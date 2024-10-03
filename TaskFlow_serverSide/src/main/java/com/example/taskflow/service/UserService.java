package com.example.taskflow.service;

import java.util.Optional;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DTOs.UserWithInfoDTO;
import com.example.taskflow.DomainModel.EntityFactory;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.Mappers.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrganizationDAO organizationDao;

    public UserDTO createUser(UserWithInfoDTO userWithInfoDTO) {
        User user = EntityFactory.getUser();

        if (this.userDAO.findByUsername(userWithInfoDTO.getUsername()).isPresent()){
            throw new IllegalArgumentException("Username already exists");
        }

        if (this.userInfoDAO.findByEmail(userWithInfoDTO.getEmail()).isPresent()){
            throw new IllegalArgumentException("Email already exists");
        }

        user.setUsername(userWithInfoDTO.getUsername());

        UserInfo userInfo = EntityFactory.getUserInfo();

        userInfo.setEmail(userWithInfoDTO.getEmail());

        userInfo.setPassword(userWithInfoDTO.getPassword());

        user.setUserInfo(userInfo);

        userInfoDAO.save(userInfo);

        user = userDAO.save(user);

        return userMapper.toDto(user);
    }

    public Optional<User> login(String username, String password) {
        Optional<User> userOptional = userDAO.findByUsername(username);
        
        // Verifica se l'utente esiste e se la password è corretta
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getUserInfo().getPassword())) {
                return Optional.of(user); // Login riuscito
            }
        }
        
        return Optional.empty(); // Login fallito
    }

    public UserDTO updateUser(String userId, UserWithInfoDTO userWithInfoDto) {
        User user = this.userDAO.findById(userId).orElse(null);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (userWithInfoDto.getEmail() != null) {
            user.setEmail(userWithInfoDto.getEmail());
        }
        if (userWithInfoDto.getUsername() != null) {
            user.setUsername(userWithInfoDto.getUsername());
        }
        if (userWithInfoDto.getPassword() != null) {
            user.getUserInfo().setPassword(userWithInfoDto.getPassword());
            this.userInfoDAO.save(user.getUserInfo());
        }

        this.userDAO.save(user);

        return this.userMapper.toDto(user);
    }

    public UserDTO getUserById(String userId) {
        User usr = this.userDAO.findById(userId).orElse(null);

        if (usr == null) {
            throw new IllegalArgumentException("User not found");
        }
        return this.userMapper.toDto(usr);
    }

    public void deleteUserById(String userId) throws IllegalArgumentException{
        if (userId == null){
            throw new IllegalArgumentException("targetId provided is null");
        }

        User usr = this.userDAO.findById(userId).orElse(null);
        if (usr == null){
            throw new IllegalArgumentException("User: " + userId + " not found");
        }

        ArrayList<Organization> organizationsOfUser = this.organizationDao.getOrganizationByUser(userId);

        String exceptionMessage = "";

        for (Organization organization : organizationsOfUser){
            try{
                this.checkOrganizationOwners(organization, usr);
                organization.removeGenericUser(usr);
                this.organizationDao.save(organization);
            }
            catch(IllegalArgumentException exception){
                exceptionMessage +=  exception.getMessage() + "\n";
            }
        }

        if (exceptionMessage != ""){
            throw new IllegalArgumentException(exceptionMessage);
        }

        this.userInfoDAO.delete(usr.getUserInfo());
        this.userDAO.delete(usr);
    }

    public UserDTO makeOwner(String organizationId, String targetId){
        User user = this.userDAO.findById(targetId).orElse(null);
        if (user == null){
            throw new IllegalArgumentException("User with targetId not found");
        }

        Organization organization = this.organizationDao.findById(organizationId).orElse(null);
        if (organization == null){
            throw new IllegalArgumentException("Organization not found");
        }

        if (!(organization.getOwners().contains(user))){
            organization.addOwner(user);
            organization.removeMember(user);
            this.organizationDao.save(organization);
        }

        return this.userMapper.toDto(user);
    }

    public UserDTO getUser(String userId){
        User user = this.userDAO.findById(userId).orElse(null);

        if (user == null){
            throw new IllegalArgumentException("User not found");
        }

        return this.userMapper.toDto(user);
    }

    private void checkOrganizationOwners(Organization organization, User user) throws IllegalArgumentException{
        ArrayList<User> owners = organization.getOwners();
        if (owners.size() == 1 && owners.contains(user)){
            throw new IllegalArgumentException("Organization: " + organization.getName() + " cannot live without owners and user " + user.getUsername() + " is the last. " +user.getUsername() + " cannot be deleted");
        }
    }
}
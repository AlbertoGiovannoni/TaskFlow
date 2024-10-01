package com.example.taskflow.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DTOs.UserWithInfoDTO;
import com.example.taskflow.DomainModel.EntityFactory;
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

    public UserDTO createUser(UserWithInfoDTO userWithInfoDTO) {

        // Mappa il DTO in un oggetto User (senza UserInfo)
        //User user = this.userMapper.toEntity(userWithInfoDTO);
        User user = EntityFactory.getUser();
        user.setUsername(userWithInfoDTO.getUsername());

        // Creazione dell'oggetto UserInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(userWithInfoDTO.getEmail());
        userInfo.setPassword(userWithInfoDTO.getPassword()); // Aggiungi qui la logica di cifratura se necessario

        // Associazione di UserInfo a User
        user.setUserInfo(userInfo);

        // Salva UserInfo
        userInfoDAO.save(userInfo);

        // Salva l'utente nel database
        User savedUser = userDAO.save(user);

        // Mappa l'entità salvata a UserDTO
        UserDTO savedUserDTO = userMapper.toDto(savedUser);

        // Restituisci l'utente creato come risposta con codice HTTP 201 (CREATED)
        return savedUserDTO;
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

    public UserDTO updateUser(UserWithInfoDTO userDto) {
        User user = this.userDAO.findById(userDto.getId()).orElseThrow();

        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }
        if (userDto.getPassword() != null) {
            user.getUserInfo().setPassword(userDto.getPassword());
            this.userInfoDAO.save(user.getUserInfo());
        }

        this.userDAO.save(user);

        return this.userMapper.toDto(user);
    }

    public UserDTO getUserById(String userId) {
        User usr = this.userDAO.findById(userId).orElseThrow();
        return this.userMapper.toDto(usr);
    }

    public void deleteUserById(String userId) {
        User usr = this.userDAO.findById(userId).orElseThrow();

        this.userInfoDAO.delete(usr.getUserInfo());
        this.userDAO.delete(usr);
    }
}
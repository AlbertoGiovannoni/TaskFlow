package com.example.taskflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DTOs.UserWithInfoDTO;
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

    public UserDTO createUser(UserWithInfoDTO userWithInfoDTO) {        

        // Mappa il DTO in un oggetto User (senza UserInfo)
        User user = userMapper.toEntity(userWithInfoDTO);

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
    
    public UserDTO getUserById(String userId) {
        User usr = userDAO.findById(userId).orElseThrow();
        return this.userMapper.toDto(usr);
    }
}
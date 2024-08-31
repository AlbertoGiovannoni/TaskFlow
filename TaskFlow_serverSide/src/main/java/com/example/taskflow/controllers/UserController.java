package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserDAO userDAO;

    private ResponseEntity<Map<String, String>> validateFields(Map<String, String> requestBody) {
        String email = requestBody.get("email");
        String password = requestBody.get("password");
        String username = requestBody.get("username");

        Map<String, String> response = new HashMap<>();

        if (email == null || email.trim().isEmpty()) {
            response.put("message", "Email cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (password == null || password.trim().isEmpty()) {
            response.put("message", "Password cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (username == null || username.trim().isEmpty()) {
            response.put("message", "Username cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return null; // Nessun errore trovato
    }

    @PostMapping("/createUser")
    public ResponseEntity<Map<String, String>> createNewUser(@RequestBody Map<String, String> requestBody) {
        Map<String, String> response = new HashMap<>();

        ResponseEntity<Map<String, String>> validationResponse = validateFields(requestBody);

        if (validationResponse != null) {
            return validationResponse;
        }

        String email = requestBody.get("email");
        String password = requestBody.get("password");
        String username = requestBody.get("username");

        // Controllo se l'email è già utilizzata
        if (userInfoDAO.findByEmail(email).isPresent()) {
            response.put("error", "Email già esistente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        // Controllo se l'username è già utilizzata
        if (userDAO.findByUsername(username).isPresent()) {
            response.put("error", "Username già esistente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        UserInfo userInfo = new UserInfo(email, password);
        userInfoDAO.save(userInfo);
        User user = new User(userInfo, username);
        userDAO.save(user);

        response.put("message", "User creato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/updateUser")
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody Map<String, String> requestBody) {

        Map<String, String> response = new HashMap<>();
        String userId = requestBody.get("id");

        if (userId == null || userId.trim().isEmpty()) {
            response.put("message", "Id cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userDAO.findById(requestBody.get("id"));

        if (optionalUser.isPresent()) {

            ResponseEntity<Map<String, String>> validationResponse = validateFields(requestBody);

            if (validationResponse != null) {
                return validationResponse;
            }

            User user = optionalUser.get();
            UserInfo userInfo = user.getUserInfo();

            userInfo.setEmail(requestBody.get("email"));
            userInfo.setPassword(requestBody.get("password"));
            user.setUsername(requestBody.get("username"));

            userInfoDAO.save(userInfo);
            userDAO.save(user);
        } else {
            response.put("message", "User not found");
        }

        response.put("message", "User aggiornato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

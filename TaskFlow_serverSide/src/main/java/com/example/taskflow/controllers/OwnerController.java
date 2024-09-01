package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
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
import com.example.taskflow.service.OrganizationService;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserDAO userDAO;

    //TODO questo deve essere removeUser dall org. Delete in Admin

    @PostMapping("/deleteUser")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestBody Map<String, String> requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se l'utente ha il ruolo OWNER
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_OWNER"))) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Access Denied");
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

        // Logica di rimozione dell'utente
        Map<String, String> response = new HashMap<>();
        String userId = requestBody.get("id");

        if (userId == null || userId.trim().isEmpty()) {
            response.put("message", "Id cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userDAO.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserInfo userInfo = user.getUserInfo();

            userInfoDAO.delete(userInfo);
            userDAO.delete(user);
            response.put("message", "User removed");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
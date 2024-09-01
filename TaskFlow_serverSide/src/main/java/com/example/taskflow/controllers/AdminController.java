package com.example.taskflow.controllers;

import java.util.ArrayList;
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

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.service.OrganizationService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @PostMapping("/deleteUser")
    public ResponseEntity<Map<String, String>> deleteUser(@RequestBody Map<String, String> requestBody) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se l'utente ha il ruolo OWNER
        if (authentication == null || !authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
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

            // rimuovo user dalle organizzazioni
            removeFromOrganizations(user);

            userInfoDAO.delete(userInfo);
            userDAO.delete(user);
            response.put("message", "User removed");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    private void removeFromOrganizations(User user) {
        ArrayList<Organization> organizations = (ArrayList) organizationDAO.findAll();

        for (Organization org : organizations) {
            boolean modified = false;

            if (org.getMembers().remove(user)) {
                modified = true;
            }
            if (org.getOwners().remove(user)) {
                modified = true;
            }
            if (modified) {
                organizationDAO.save(org);
            }

        }
    }
}

package com.example.taskflow.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    OrganizationDAO organizationDAO;

    @DeleteMapping("/{userId}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable String userId, @RequestBody Map<String, String> requestBody) {

        // Logica di rimozione dell'utente
        Map<String, String> response = new HashMap<>();

        if (userId == null || userId.trim().isEmpty()) {
            response.put("message", "Id cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        User user = userDAO.findById(userId).orElse(null);

        if (user == null){
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        UserInfo userInfo = user.getUserInfo();

        // rimuovo user dalle organizzazioni
        removeFromOrganizations(user);

        userInfoDAO.delete(userInfo);
        userDAO.delete(user);
        response.put("message", "User removed");
        return new ResponseEntity<>(response, HttpStatus.OK);
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

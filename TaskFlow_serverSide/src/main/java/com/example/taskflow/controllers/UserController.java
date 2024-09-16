
package com.example.taskflow.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.AssigneeDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SimpleFieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SingleSelectionDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.DateData;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;
import com.example.taskflow.Mappers.UserMapper;

import jakarta.websocket.server.PathParam;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    NotificationDAO notificationDAO;

    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    ActivityDAO activityDAO;
    @Autowired
    FieldDAO fieldDAO;
    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    OrganizationDAO organizationDAO;

    @Autowired
    UserDAO userDAO;

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Access Denied: You are not authorized to perform this action.");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<Map<String, String>> validateFields(Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        String password = (String) requestBody.get("password");
        String username = (String) requestBody.get("username");

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


    @PostMapping("/user/test")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        User user = UserMapper.INSTANCE.userDTOToUser(userDTO);
        userDAO.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.INSTANCE.userToUserDTO(user));
    }

    @PostMapping("/public/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, Object> requestBody) {
        Map<String, String> response = new HashMap<>();

        ResponseEntity<Map<String, String>> validationResponse = validateFields(requestBody);

        if (validationResponse != null) {
            return validationResponse;
        }

        String email = (String) requestBody.get("email");
        String password = (String) requestBody.get("password");
        String username = (String) requestBody.get("username");

        // Controllo se l'email è già utilizzata
        if (userInfoDAO.findByEmail(email).isPresent()) {
            response.put("error", "Email già esistente");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Controllo se l'username è già utilizzata
        if (userDAO.findByUsername(username).isPresent()) {
            response.put("error", "Username già esistente");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        UserInfo userInfo = new UserInfo(email, password);
        userInfoDAO.save(userInfo);
        User user = new User(userInfo, username);
        userDAO.save(user);

        response.put("message", "User creato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable String userId,
            @RequestBody Map<String, Object> requestBody) {

        Map<String, String> response = new HashMap<>();

        if (userId == null || userId.trim().isEmpty()) {
            response.put("message", "userId non può essere vuoto");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userDAO.findById(userId);

        if (optionalUser.isPresent()) {

            ResponseEntity<Map<String, String>> validationResponse = validateFields(requestBody);

            if (validationResponse != null) {
                return validationResponse;
            }

            User user = optionalUser.get();
            UserInfo userInfo = user.getUserInfo();

            userInfo.setEmail((String) requestBody.get("email"));
            userInfo.setPassword((String) requestBody.get("password"));
            user.setUsername((String) requestBody.get("username"));

            userInfoDAO.save(userInfo);
            userDAO.save(user);
        } else {
            response.put("message", "User non trovato");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        response.put("message", "User aggiornato");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @DeleteMapping("/user/{userId}/myOrganization/{organizationId}/users/{targetId}")
    public ResponseEntity<Map<String, String>> removeUser(@PathVariable String targetId, @PathVariable String organizationId, @RequestBody Map<String, String> requestBody) {
        
        Map<String, String> response = new HashMap<>();
    
        if (targetId == null || targetId.trim().isEmpty() || organizationId == null || organizationId.trim().isEmpty()) {
            response.put("message", "User ID and Organization ID cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Trova l'organizzazione
        Optional<Organization> optionalOrganization = organizationDAO.findById(organizationId);
        if (!optionalOrganization.isPresent()) {
            response.put("message", "Organization not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Organization organization = optionalOrganization.get();

        // Trova l'utente da rimuovere
        User target = userDAO.findById(targetId).orElse(null);

        if (target == null){
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
            
        return removeUserFromOrg(target, organization);
    }

    private ResponseEntity<Map<String, String>> removeUserFromOrg(User target,
            Organization organization) {
        

        // Rimuove l'utente dai membri o proprietari dell'organizzazione specifica
        boolean userRemoved = false;
        if (organization.getOwners().contains(target)) {
            organization.removeOwner(target);
            userRemoved = true;
        }
        if (organization.getMembers().contains(target)) {
            organization.removeMember(target);
            userRemoved = true;
        }

        // Salva l'organizzazione aggiornata
        if (userRemoved) {
            organizationDAO.save(organization);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User removed from the organization");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not found in the specified organization");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}

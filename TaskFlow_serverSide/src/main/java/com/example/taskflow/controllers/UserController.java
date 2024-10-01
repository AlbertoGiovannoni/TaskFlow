
package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.validation.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import com.example.taskflow.DTOs.UserWithInfoDTO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.Mappers.UserMapper;
import com.example.taskflow.service.UserService;

import jakarta.validation.Valid;

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
    UserMapper userMapper;

    @Autowired
    UserService userService;

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

    
    @PostMapping("/public/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserWithInfoDTO userWithInfoDTO) {

        //Controllo se l'email è già utilizzata
        if (userInfoDAO.findByEmail(userWithInfoDTO.getEmail()).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Email già esistente");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }

        //Controllo se l'username è già utilizzata
        if (userDAO.findByUsername(userWithInfoDTO.getUsername()).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Username già esistente");
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse);
        }

        UserDTO userDto = userService.createUser(userWithInfoDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserWithInfoDTO userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        Optional<User> userOptional = userService.login(username, password);
        
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(this.userMapper.toDto((userOptional.get())));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid username or password");
        }
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

            ResponseEntity<Map<String, String>> validationResponse = validateFields(requestBody); //TODO da togliere, viene fatto in @Valid

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
    public ResponseEntity<Map<String, String>> removeUser(@PathVariable String targetId,
            @PathVariable String organizationId, @RequestBody Map<String, String> requestBody) {

        Map<String, String> response = new HashMap<>();

        if (targetId == null || targetId.trim().isEmpty() || organizationId == null
                || organizationId.trim().isEmpty()) {
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

        if (target == null) {
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) ->{
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}

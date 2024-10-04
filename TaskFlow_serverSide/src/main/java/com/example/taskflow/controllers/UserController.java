
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
import com.example.taskflow.DomainModel.User;
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

    @PostMapping("/public/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserWithInfoDTO userWithInfoDTO) {

        UserDTO userDto;

        try {
            userDto = this.userService.createUser(userWithInfoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
        }
        catch(Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid username or password");
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId)")
    @PatchMapping("/user/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable String userId, @RequestBody UserWithInfoDTO userWithInfoDTO) {
        try{
            UserDTO userUpdated = this.userService.updateUser(userId, userWithInfoDTO);
            return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
        }
        catch(Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage() + "USER_ID: " + userWithInfoDTO.getId() + "     " + userId);
        }
    }

    
    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') && @checkUriService.check(authentication, #userId, #organizationId)")
    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/users")
    public ResponseEntity<?> makeOwner(@PathVariable String userId, @PathVariable String organizationId, @RequestParam String targetId){
        try {
            UserDTO userUpdated = this.userService.makeOwner(organizationId, targetId);
            return ResponseEntity.status(HttpStatus.OK).body(userUpdated);
        }
        catch (Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') && @checkUriService.check(authentication, #userId, #organizationId)")
    @DeleteMapping("/user/{userId}/myOrganization/{organizationId}/users")
    public ResponseEntity<String> removeUser(@PathVariable String organizationId, @PathVariable String userId, @RequestParam String targetId) {
        try{
            this.userService.deleteUserById(targetId);
            return ResponseEntity.status(HttpStatus.OK).body("User " + targetId + " removed");
        }
        catch(Exception exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId)")
    @GetMapping("user/{userId}")
    public ResponseEntity<?> getUser(@PathVariable String userId){
        try{
            UserDTO userDto = this.userService.getUser(userId);
            return ResponseEntity.status(HttpStatus.OK).body(userDto);
        }
        catch(Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
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

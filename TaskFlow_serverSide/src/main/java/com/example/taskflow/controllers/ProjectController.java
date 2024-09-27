package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.service.ProjectService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class ProjectController {

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    ProjectService projectService;


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Access Denied: You are not authorized to perform this action.");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}")
    public ResponseEntity<ProjectDTO> createActivity(@Valid @RequestBody ActivityDTO activityDTO,
            @PathVariable String projectId, @PathVariable String organizationId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.projectService.addActivityToProject(projectId, activityDTO));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PatchMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/renameProject")
    public ResponseEntity<ProjectDTO> renameProject(@PathVariable String projectId,
            @RequestBody Map<String, String> requestBody, @PathVariable String organizationId) {
        String newName = requestBody.get("newName");
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("newName cannot be empty");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.projectService.renameProject(projectId, newName));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/addFieldDefinitionToProject")
    public ResponseEntity<?> addFieldDefinitionToProject(@PathVariable String projectId, @RequestBody FieldDefinitionDTO fieldDefinitionDTO, @PathVariable String organizationId) {
        
        return ResponseEntity.status(HttpStatus.OK)
                 .body(this.projectService.addFieldDefinitionToProject(projectId, fieldDefinitionDTO));
    }

    @GetMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable String projectId, @PathVariable String organizationId) {
        return ResponseEntity.status(HttpStatus.OK)
                 .body(this.projectService.getProjectById(projectId));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @DeleteMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}")
    public ResponseEntity<ProjectDTO> removeActivity(@PathVariable String projectId, @PathVariable String activityId, @PathVariable String organizationId) {
        return ResponseEntity.status(HttpStatus.OK)
                 .body(this.projectService.removeActivity(projectId, activityId));
    }

    // TODO deleteActivity
}

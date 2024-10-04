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

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.service.ProjectService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

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

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId)")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities")
    public ResponseEntity<?> createActivity(@Valid @RequestBody ActivityDTO activityDTO,
                                            @PathVariable String userId, 
                                            @PathVariable String organizationId, 
                                            @PathVariable String projectId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.projectService.addActivityToProject(projectId, activityDTO));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') && @checkUriService.check(authentication, #userId, #organizationId, #projectId)")
    @PatchMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}")
    public ResponseEntity<?> renameProject(@PathVariable String userId, 
                                           @PathVariable String organizationId, 
                                           @PathVariable String projectId, 
                                           @RequestParam String newName) 
    {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.projectService.renameProject(projectId, newName));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId)")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/fieldDefinitions")
    public ResponseEntity<?> addFieldDefinitionToProject(@RequestBody FieldDefinitionDTO fieldDefinitionDTO, 
                                                         @PathVariable String userId, 
                                                         @PathVariable String organizationId, 
                                                         @PathVariable String projectId) 
    {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.projectService.addFieldDefinitionToProject(projectId, fieldDefinitionDTO));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId)")
    @GetMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}")
    public ResponseEntity<?> getProject(@PathVariable String userId,
                                        @PathVariable String organizationId,
                                        @PathVariable String projectId) 
    {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.projectService.getProjectById(projectId));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

}

package com.example.taskflow.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.service.ProjectService;

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

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects")
    public ResponseEntity<Map<String, String>> createProject(@RequestBody Map<String, Object> requestBody) {

        Map<String, String> response = new HashMap<>();
        String name = (String) requestBody.get("projectName");

        // Controllo e conversione dell'input
        if (name == null || name.trim().isEmpty()) {
            response.put("message", "projectName cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ProjectDTO projectDto = new ProjectDTO( null, name, new ArrayList<FieldDefinitionDTO>(), new ArrayList<ActivityDTO>());
        this.projectService.createProject(projectDto);

        response.put("message", "Progetto creato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}")
    public ResponseEntity<Map<String, String>> renameProject(@PathVariable String projectId, @RequestBody Map<String, Object> requestBody) {

        Map<String, String> response = new HashMap<>();

        String name = (String) requestBody.get("newName");

        // Controllo e conversione dell'input
        if (name == null || name.trim().isEmpty()) {
            response.put("message", "projectName cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (projectId == null) {
            response.put("message", "projectId cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ProjectDTO projectDto = projectService.renameProject(projectId, name);

        if (projectDto == null) {
            response.put("message", "wrong projectId");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("message", "Progetto rinominato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

package com.example.taskflow.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.service.OrganizationService;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/user")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationDAO organizationDAO;

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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Access Denied: You are not authorized to perform this action.");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId)")
    @PostMapping("/{userId}/myOrganization")
    public ResponseEntity<?> createOrganization(@Valid @RequestBody OrganizationDTO organizationDTO,
            @PathVariable String userId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.organizationService.createNewOrganization(userId, organizationDTO));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') && @checkUriService.check(authentication, #userId, #organizationId)")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects")
    public ResponseEntity<?> addProjectToOrganization(@Valid @RequestBody ProjectDTO projectDTO,
            @PathVariable String organizationId,
            @PathVariable String userId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.organizationService.addNewProjectToOrganization(organizationId, projectDTO));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') && @checkUriService.check(authentication, #userId, #organizationId)")
    @PatchMapping("/{userId}/myOrganization/{organizationId}/addMember")
    public ResponseEntity<?> addMemberToOrganization(@RequestParam String targetId,
            @PathVariable String organizationId,
            @PathVariable String userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.organizationService.addMemberToOrganization(organizationId, targetId));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') && @checkUriService.check(authentication, #userId, #organizationId)")
    @PatchMapping("/{userId}/myOrganization/{organizationId}/addOwner")
    public ResponseEntity<?> addOwnerToOrganization(@RequestParam String targetId,
            @PathVariable String organizationId,
            @PathVariable String userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.organizationService.addOwnerToOrganization(organizationId, targetId));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId)")
    @GetMapping("/{userId}/myOrganization/{organizationId}")
    public ResponseEntity<?> getOrganizationById(@PathVariable String organizationId, @PathVariable String userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.organizationService.getOrganizationById(organizationId));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') && @checkUriService.check(authentication, #userId, #organizationId)")
    @DeleteMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}")
    public ResponseEntity<?> deleteProjectFromOrganization(@PathVariable String organizationId,
            @PathVariable String projectId,
            @PathVariable String userId) {
        try {
            OrganizationDTO organizationDTO = this.organizationService.deleteProjectFromOrganization(organizationId,
                    projectId);
            return ResponseEntity.status(HttpStatus.OK).body(organizationDTO);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId)")
    @GetMapping("/{userId}/myOrganization")
    public ResponseEntity<?> getMyOrganizations(@PathVariable String userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.organizationDAO.getOrganizationByUser(userId));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER') && @checkUriService.check(authentication, #userId, #organizationId)")
    @DeleteMapping("/{userId}/myOrganization/{organizationId}")
    public ResponseEntity<?> deleteOrganizationById(@PathVariable String organizationId, @PathVariable String userId) {
        try {
            this.organizationService.deleteOrganization(organizationId);
            return ResponseEntity.status(HttpStatus.OK).body("Organization deleted");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId)")
    @GetMapping("/{userId}/myOrganization/{organizationId}/users")
    public ResponseEntity<?> getOrganizationUsers(@PathVariable String organizationId, @PathVariable String userId) {
        try {
            ArrayList<UserDTO> users = this.organizationService.getAllOrganizationUserDTO(organizationId);
            return ResponseEntity.status(HttpStatus.OK).body(users);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
}

package com.example.taskflow.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.service.OrganizationService;
import com.example.taskflow.service.ProjectService;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/user")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationDAO organizationDAO;
    @Autowired
    private ProjectService projectService;

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

    @PostMapping("/{userId}/myOrganization")
    public ResponseEntity<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationDTO organizationDTO,
            @PathVariable String userId) {
        ArrayList<String> owners = new ArrayList<String>();
        owners.add(userId);
        organizationDTO.setOwnersId(owners);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.organizationService.createNewOrganization(organizationDTO));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects")
    public ResponseEntity<OrganizationDTO> addProjectToOrganization(@Valid @RequestBody ProjectDTO projectDTO,
            @PathVariable String organizationId) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.organizationService.addNewProjectToOrganization(organizationId, projectDTO));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PatchMapping("/{userId}/myOrganization/{organizationId}/addMember")
    public ResponseEntity<OrganizationDTO> addMemberToOrganization(@RequestBody Map<String, String> requestBody,
            @PathVariable String organizationId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.organizationService.addMemberToOrganization(organizationId, requestBody.get("targetId")));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PatchMapping("/{userId}/myOrganization/{organizationId}/addOwner")
    public ResponseEntity<OrganizationDTO> addOwnerToOrganization(@RequestBody Map<String, String> requestBody,
            @PathVariable String organizationId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.organizationService.addOwnerToOrganization(organizationId, requestBody.get("targetId")));
    }

    @GetMapping("/{userId}/myOrganization/{organizationId}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable String organizationId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.organizationService.getOrganizationById(organizationId));
    }

    @GetMapping("/{userId}/myOrganization")
    public ResponseEntity<ArrayList<Organization>> getMyOrganizations(@PathVariable String userId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.organizationDAO.getOrganizationByUser(userId));
    }

    @DeleteMapping("/{userId}/myOrganization/{organizationId}")
    public ResponseEntity<String> deleteOrganizationById(@PathVariable String organizationId) {
        this.organizationService.deleteOrganization(organizationId);
        return ResponseEntity.status(HttpStatus.OK).body("deleted");
    }

}

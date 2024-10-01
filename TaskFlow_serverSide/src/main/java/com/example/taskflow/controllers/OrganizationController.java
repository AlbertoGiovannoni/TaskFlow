package com.example.taskflow.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DTOs.ProjectDTO;
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
    public ResponseEntity<OrganizationDTO> addMemberToOrganization(@RequestParam String targetId,
            @PathVariable String organizationId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.organizationService.addMemberToOrganization(organizationId, targetId));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PatchMapping("/{userId}/myOrganization/{organizationId}/addOwner")
    public ResponseEntity<OrganizationDTO> addOwnerToOrganization(@RequestParam String targetId,
            @PathVariable String organizationId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.organizationService.addOwnerToOrganization(organizationId, targetId));
    }

    @GetMapping("/{userId}/myOrganization/{organizationId}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable String organizationId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.organizationService.getOrganizationById(organizationId));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @DeleteMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}")
    public ResponseEntity<OrganizationDTO> deleteProjectFromOrganization(@PathVariable String organizationId, @PathVariable String projectId) {
        OrganizationDTO organizationDTO = this.organizationService.deleteProjectFromOrganization(organizationId, projectId);
        return ResponseEntity.status(HttpStatus.OK).body(organizationDTO);
    }

    @GetMapping("/{userId}/myOrganization")
    public ResponseEntity<ArrayList<Organization>> getMyOrganizations(@PathVariable String userId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.organizationDAO.getOrganizationByUser(userId));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @DeleteMapping("/{userId}/myOrganization/{organizationId}")
    public ResponseEntity<String> deleteOrganizationById(@PathVariable String organizationId) {
        this.organizationService.deleteOrganization(organizationId);
        return ResponseEntity.status(HttpStatus.OK).body("Organization deleted");
    }

}

package com.example.taskflow.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.service.OrganizationService;
import com.example.taskflow.service.ProjectService;

import jakarta.validation.Valid;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ProjectService projectService;

    @PostMapping("/{userId}/myOrganization")
    public ResponseEntity<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationDTO organizationDTO, @PathVariable String userId) {
        ArrayList<String> owners = new ArrayList<String>();
        owners.add(userId);
        organizationDTO.setOwnersId(owners);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.organizationService.createNewOrganization(organizationDTO));
    }

    @PreAuthorize("@dynamicRoleService.getRolesBasedOnContext(#organizationId, authentication).contains('ROLE_OWNER')")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO, @PathVariable String organizationId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.projectService.pushNewProject(projectDTO));
    }
}

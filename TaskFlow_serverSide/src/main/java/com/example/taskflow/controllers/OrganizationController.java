package com.example.taskflow.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.service.OrganizationService;

import jakarta.validation.Valid;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/user")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping("/{userId}/myOrganization")
    public ResponseEntity<OrganizationDTO> createOrganization(@Valid @RequestBody OrganizationDTO organizationDTO, @PathVariable String userId) {
        ArrayList<String> owners = new ArrayList<String>();
        owners.add(userId);
        organizationDTO.setOwnersId(owners);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.organizationService.createNewOrganization(organizationDTO));
    }

}

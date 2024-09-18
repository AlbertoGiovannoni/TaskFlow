package com.example.taskflow.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionServiceManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/users")
public class FieldDefinitionController {
    @Autowired
    private FieldDefinitionServiceManager fieldDefinitionServiceManager;

    @PostMapping("/myOrganizations/{organizationId}/projects/newFieldDefinition")
    public ResponseEntity<FieldDefinitionDTO> createNewFieldDefinition(
        @PathVariable String organizationId,
        @PathVariable String projectId,
        @Valid @RequestBody FieldDefinitionDTO fieldDefinitionDto
    ){  
        FieldDefinitionDTO createdFieldDefinitionDto = this.fieldDefinitionServiceManager
                                                        .getFieldDefinitionService(fieldDefinitionDto)
                                                        .createFieldDefinition(fieldDefinitionDto);

        return ResponseEntity.status(HttpStatus.OK).body(createdFieldDefinitionDto);
    }
}

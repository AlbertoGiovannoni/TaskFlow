package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionService;

@RestController
@RequestMapping("/rest/users")
public class FieldDefinitionController {
    @Autowired
    FieldDefinitionService fieldDefinitionService;

    @PostMapping("/myOrganizations/{organizationId}/projects/newFieldDefinition")
    public ResponseEntity<FieldDefinitionDTO> createNewFieldDefinition(
        @PathVariable String organizationId,
        @PathVariable String projectId,
        @Valid @RequestBody FieldDefinitionDTO fieldDefinitionDto
    ){  
        FieldDefinitionDTO createdFieldDefinitionDto = this.fieldDefinitionService.createFieldDefinition(fieldDefinitionDto);

        return ResponseEntity.status(HttpStatus.OK).body(createdFieldDefinitionDto);
    }
}

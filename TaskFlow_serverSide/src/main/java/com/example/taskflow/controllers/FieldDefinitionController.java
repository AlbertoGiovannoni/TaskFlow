package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;

@RestController
@RequestMapping("/rest/users")
public class FieldDefinitionController {
    @PatchMapping("/myOrganizations/{organizationId}/projects/{projectId}")
    public ResponseEntity<FieldDefinition> createNewFieldDefinition(
        @PathVariable String organizationId,
        @PathVariable String projectId,
        @RequestBody FieldDefinitionDTO fieldDefinitionDto
    ){  

        // FIXME

        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(fieldDefinitionDto.getType())
                                            .setName(fieldDefinitionDto.getName())
                                            .addMultipleEntry(fieldDefinitionDto.getAllEntries())
                                            .build();

        return new ResponseEntity<>(fieldDefinition, HttpStatus.OK);
    }
}

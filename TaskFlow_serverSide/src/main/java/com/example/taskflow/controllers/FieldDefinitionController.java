package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DTOs.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionService;

@RestController
@RequestMapping("/rest/users")
public class FieldDefinitionController {
    @Autowired
    FieldDefinitionService fieldDefinitionService;
    
    @PostMapping("/myOrganizations/{organizationId}/projects/newFieldDefinition")
    public ResponseEntity<FieldDefinition> createNewFieldDefinition(
        @PathVariable String organizationId,
        @PathVariable String projectId,
        @RequestBody FieldDefinitionDTO fieldDefinitionDto
    ){  

        // FIXME: La costruzione dovrebbe essere fatta nel service 
        // Quindi il service riceve DTOs? Secondo me si quindi con i mappers
        // interagiscono solo i services. 
        FieldDefinitionBuilder fieldDefinitionBuilder = FieldDefinitionFactory.getBuilder(fieldDefinitionDto.getType())
                                            .setName(fieldDefinitionDto.getName());

        if (fieldDefinitionDto.getParameters() != null){
            if (!fieldDefinitionDto.getParameters().isEmpty()){
                fieldDefinitionBuilder.addParameters(fieldDefinitionDto.getParameters());
            }
        }
        
        FieldDefinition fieldDefinition = fieldDefinitionBuilder.build();

        fieldDefinition = this.fieldDefinitionService.createFieldDefinition(fieldDefinition);

        return new ResponseEntity<>(fieldDefinition, HttpStatus.OK);
    }
}

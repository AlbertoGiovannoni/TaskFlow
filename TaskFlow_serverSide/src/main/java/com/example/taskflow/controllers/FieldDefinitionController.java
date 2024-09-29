package com.example.taskflow.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionServiceManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class FieldDefinitionController {
    @Autowired
    private FieldDefinitionServiceManager fieldDefinitionServiceManager;

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

    @PostMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/newFieldDefinition")
    public ResponseEntity<FieldDefinitionDTO> addNewFieldDefinition(
            @PathVariable String organizationId,
            @PathVariable String projectId,
            @Valid @RequestBody FieldDefinitionDTO fieldDefinitionDto) {
        FieldDefinitionDTO createdFieldDefinitionDto = this.fieldDefinitionServiceManager
                .getFieldDefinitionService(fieldDefinitionDto)
                .pushNewFieldDefinitionDTO(fieldDefinitionDto, projectId);

        return ResponseEntity.status(HttpStatus.OK).body(createdFieldDefinitionDto);
    }

    @DeleteMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/{fieldDefinitionId}")
    public ResponseEntity<String> removeFieldDefinition(@PathVariable String organizationId,
            @PathVariable String projectId, @PathVariable String fieldDefinitionId) {
        this.fieldDefinitionServiceManager
                .getFieldDefinitionService(fieldDefinitionId)
                .deleteFieldDefinitionAndFieldsAndReferenceToFieldsInActivity(fieldDefinitionId);

        return ResponseEntity.status(HttpStatus.OK).body(fieldDefinitionId);
    }
}

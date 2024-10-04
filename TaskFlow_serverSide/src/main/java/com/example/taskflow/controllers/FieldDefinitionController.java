package com.example.taskflow.controllers;

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

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId)")
    @PostMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/newFieldDefinition")
    public ResponseEntity<?> addNewFieldDefinition(
            @PathVariable String userId,
            @PathVariable String organizationId,
            @PathVariable String projectId,
            @Valid @RequestBody FieldDefinitionDTO fieldDefinitionDto) {

        try {
            FieldDefinitionDTO createdFieldDefinitionDto = this.fieldDefinitionServiceManager
                    .getFieldDefinitionService(fieldDefinitionDto)
                    .pushNewFieldDefinitionDTO(fieldDefinitionDto, projectId);

            return ResponseEntity.status(HttpStatus.OK).body(createdFieldDefinitionDto);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.checkFieldDefinition(authentication, #userId, #organizationId, #projectId, #fieldDefinitionId)")
    @DeleteMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/{fieldDefinitionId}")
    public ResponseEntity<?> removeFieldDefinition(@PathVariable String organizationId,
            @PathVariable String projectId, @PathVariable String fieldDefinitionId, @PathVariable String userId) {
        try {
            this.fieldDefinitionServiceManager
                    .getFieldDefinitionService(fieldDefinitionId)
                    .deleteFieldDefinitionAndFieldsAndReferenceToFieldsInActivity(fieldDefinitionId);

            return ResponseEntity.status(HttpStatus.OK).body(fieldDefinitionId);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }
}

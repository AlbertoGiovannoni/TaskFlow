
package com.unifi.taskflow.businessLogic.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.services.ActivityService;
import com.unifi.taskflow.businessLogic.services.fieldServices.FieldServiceManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ActivityController {
    @Autowired
    ActivityService activityService;
    @Autowired
    FieldServiceManager fieldServiceManager;

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

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId, #activityId)")
    @DeleteMapping("/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}")
    public ResponseEntity<?> removeActivity(@PathVariable String userId,
                                            @PathVariable String organizationId,
                                            @PathVariable String projectId, 
                                            @PathVariable String activityId) 
    {
        try {
            this.activityService.deleteActivityAndFields(activityId);
            return ResponseEntity.status(HttpStatus.OK).body(activityId + " removed");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId, #activityId)")
    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}")
    public ResponseEntity<?> renameActivity(
            @PathVariable String userId,
            @PathVariable String organizationId,
            @PathVariable String projectId,
            @PathVariable String activityId,
            @RequestParam String newName) {

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.activityService.renameActivity(activityId, newName));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId, #activityId)")
    @PostMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/field")
    public ResponseEntity<?> addField(
            @PathVariable String userId,
            @PathVariable String organizationId,
            @PathVariable String projectId,
            @PathVariable String activityId,
            @Valid @RequestBody FieldDTO fieldDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.activityService.addFieldToActivity(activityId, fieldDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId, #activityId)")
    @PostMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields")
    public ResponseEntity<?> addFields(
            @PathVariable String userId,
            @PathVariable String organizationId,
            @PathVariable String projectId,
            @PathVariable String activityId,
            @Valid @RequestBody ArrayList<FieldDTO> fieldDtos) {

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.activityService.addFieldsToActivity(activityId, fieldDtos));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}

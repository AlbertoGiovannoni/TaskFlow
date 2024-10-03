
package com.example.taskflow.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.service.ActivityService;
import com.example.taskflow.service.FieldService.FieldServiceManager;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ActivityController {

    @Autowired
    UserInfoDAO userInfoDAO;
    @Autowired
    ActivityService activityService;
    @Autowired
    NotificationDAO notificationDAO;
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    ActivityDAO activityDAO;
    @Autowired
    FieldDAO fieldDAO;
    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    FieldServiceManager fieldServiceManager;
    @Autowired
    UserDAO userDAO;

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

    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}")
    public ResponseEntity<?> renameActivity(@PathVariable String activityId,
            @RequestParam String newName) {

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.activityService.renameActivity(activityId, newName));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PostMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields")
    public ResponseEntity<?> addField(@PathVariable String activityId,
            @Valid @RequestBody FieldDTO fieldDto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.activityService.addFieldToActivity(activityId, fieldDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PostMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/addFields")
    public ResponseEntity<?> addFields(@PathVariable String activityId,
            @Valid @RequestBody ArrayList<FieldDTO> fieldDtos) {

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.activityService.addFieldsToActivity(activityId, fieldDtos));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/{fieldId}")
    public ResponseEntity<?> updateField(@PathVariable String fieldId,
            @RequestBody FieldDTO fieldDto) {

        fieldDto.setId(fieldId);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.activityService.updateField(fieldDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/updateFields")
    public ResponseEntity<?> updateFields(@RequestBody ArrayList<FieldDTO> fieldDtos) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.activityService.updateFields(fieldDtos));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/{fieldId}")
    public ResponseEntity<?> removeField(@PathVariable String fieldId, @PathVariable String activityId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.activityService.removeField(activityId, fieldId));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}

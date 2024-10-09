package com.unifi.taskflow.businessLogic.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.services.ActivityService;
import com.unifi.taskflow.businessLogic.services.fieldServices.FieldServiceManager;

@RestController
@RequestMapping("/api/user")
public class FieldController {
    @Autowired
    private FieldServiceManager fieldServiceManager;
    @Autowired
    private ActivityService activityService;

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId, #activityId, #fieldId)")
    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/{fieldId}")
    public ResponseEntity<?> updateField(
            @PathVariable String fieldId,
            @PathVariable String userId,
            @PathVariable String organizationId,
            @PathVariable String projectId,
            @PathVariable String activityId,
            @RequestBody FieldDTO fieldDto) 
    {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.fieldServiceManager.getFieldService(fieldId).updateField(fieldDto));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId, #activityId)")
    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/updateFields")
    public ResponseEntity<?> updateFields(
            @RequestBody ArrayList<FieldDTO> fieldDtos,
            @PathVariable String userId,
            @PathVariable String organizationId,
            @PathVariable String projectId,
            @PathVariable String activityId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.activityService.updateFields(fieldDtos));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @PreAuthorize("@checkUriService.check(authentication, #userId, #organizationId, #projectId, #activityId, #fieldId)")
    @DeleteMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/{fieldId}")
    public ResponseEntity<?> removeField(
            @PathVariable String userId,
            @PathVariable String organizationId,
            @PathVariable String projectId,
            @PathVariable String activityId,
            @PathVariable String fieldId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.activityService.removeField(activityId, fieldId));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}

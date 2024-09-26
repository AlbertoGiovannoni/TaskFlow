
package com.example.taskflow.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.AssigneeDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SimpleFieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SingleSelectionDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.service.ActivityService;
import com.example.taskflow.service.FieldService.FieldServiceManager;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

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

    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}")
    public ResponseEntity<Activity> renameActivity(@PathVariable String activityId,
            @RequestBody Map<String, String> requestBody) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.activityService.renameActivity(activityId, requestBody.get("newName")));
    }

    @PostMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields")
    public ResponseEntity<ActivityDTO> addField(@PathVariable String activityId,
            @Valid @RequestBody FieldDTO fieldDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.activityService.addFieldToActivity(activityId, fieldDto));
    }

    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/{fieldId}")
    public ResponseEntity<FieldDTO> updateField(@PathVariable String fieldId,
            @RequestBody FieldDTO fieldDto) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.activityService.updateField(fieldDto));
    }

    @DeleteMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/{fieldId}")
    public ResponseEntity<ActivityDTO> removeField(@PathVariable String fieldId){
        return null;
        //this.activityService.
    }

    // TODO rimuovi field e rimuovi attività
}


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
    UserDAO userDAO;
    
     
    @PostMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities")
    public ResponseEntity<Map<String, String>> createNewActivity(@Valid @RequestBody ActivityDTO activityDTO, @PathVariable String projectId) {

        Map<String, String> response = new HashMap<>();

        this.activityService.pushNewActivity(activityDTO, projectId);

        String name = (String) requestBody.get("activityName");
        Object fieldsObject = requestBody.get("activityFields");

        // Controllo e conversione dell'input
        if (name == null || fieldsObject == null || name.trim().isEmpty()) {
            response.put("message", "activityName e activityFields non possono essere vuoti");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verifica che activityFields sia un array JSON
        if (!(fieldsObject instanceof ArrayList)) {
            response.put("message", "activityFields deve essere un array di JSON");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        JSONArray fieldsJson = new JSONArray((ArrayList<?>) fieldsObject);
        ArrayList<Field> fields = new ArrayList<>();
        ArrayList<?> parameter;

        JSONObject fieldJson = new JSONObject();
        for (int i = 0; i < fieldsJson.length(); i++) {
            fieldJson = fieldsJson.getJSONObject(i);

            FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(fieldJson.getString("fieldDefinitionId"))
                    .orElse(null);
            if (fieldDefinition == null) {
                response.put("message", "wrong fieldDefinitionId");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            FieldType fieldType = fieldDefinition.getType();

            parameter = convertParameter(fieldsJson.getJSONObject(i), fieldType);

            Field field = FieldFactory.getBuilder(fieldType)
                    .addFieldDefinition(fieldDefinition)
                    .addParameters(parameter)
                    .build();

            field = fieldDAO.save(field);
            fields.add(field);
        }

        Activity activity = new Activity(name, fields);
        activityDAO.save(activity);

        response.put("message", "Attività creata");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}")
    public ResponseEntity<Map<String, String>> renameActivity(@PathVariable String activityId,
            @RequestBody Map<String, Object> requestBody) {

        Map<String, String> response = new HashMap<>();

        String name = (String) requestBody.get("name");

        if (name == null || name.trim().length() == 0) {
            response.put("message", "name non poò essere vuoto");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (activityId == null || activityId.trim().isEmpty()) {
            response.put("message", "activityId non può essere vuoto");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Activity activity = activityDAO.findById(activityId).orElse(null);

        if (activity == null) {
            response.put("message", "wrong activityId");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        activity.setName(name);
        activityDAO.save(activity);

        response.put("message", "Nome activity aggiornato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // TODO fare check anche per name type e value che siano non nulli sia qui che
    // in create activity??
    @PostMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields")
    public ResponseEntity<Map<String, String>> addField(@PathVariable String activityId,
            @RequestBody Map<String, Object> requestBody) {
        Map<String, String> response = new HashMap<>();

        Object fieldObject = requestBody.get("field");

        Activity activity = activityDAO.findById(activityId).orElse(null);

        if (activity == null) {
            response.put("message", "wrong activityId");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (activityId == null || activityId.trim().isEmpty()) {
            response.put("message", "activityId non poò essere vuoto");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Controllo che field sia un JSON valido
        if (!(fieldObject instanceof Map)) {
            response.put("message", "field deve essere un JSON");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Se field è un JSON valido, convertilo in JSONObject
        JSONObject fieldJson;
        try {
            fieldJson = new JSONObject((Map<?, ?>) fieldObject);
        } catch (JSONException e) {
            response.put("message", "field deve essere un JSON valido");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ArrayList<?> parameter;

        FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(fieldJson.getString("fieldDefinitionId"))
                .orElse(null);
        if (fieldDefinition == null) {
            response.put("message", "wrong fieldDefinitionId");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        FieldType fieldType = fieldDefinition.getType();

        parameter = convertParameter(fieldJson, fieldType);

        if (fieldType == fieldType.ASSIGNEE || fieldType == fieldType.SINGLE_SELECTION) {
            fieldDefinition.addMultipleEntry(parameter); // TODO restituire fieldDefinition dal metodo di parameter?
        }

        fieldDefinitionDAO.save(fieldDefinition);

        Field field = FieldFactory.getBuilder(fieldType)
                .addFieldDefinition(fieldDefinition)
                .addParameters(parameter)
                .build();

        field = fieldDAO.save(field);

        activity.addField(field);

        activityDAO.save(activity);

        response.put("message", "Field aggiunto");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/user/{userId}/myOrganization/{organizationId}/projects/{projectId}/activities/{activityId}/fields/{fieldId}")                                                                                       // testare
    public ResponseEntity<Map<String, String>> updateField(@PathVariable String fieldId,
            @RequestBody Map<String, Object> requestBody) {
        Map<String, String> response = new HashMap<>();

        Field field = fieldDAO.findById(fieldId).orElse(null);
        ArrayList<?> parameter;
        FieldType fieldType;
        String fieldTypeString;

        if (field == null) {
            response.put("error", "Field non trovato");
            return ResponseEntity.badRequest().body(response);
        }

        System.out.println(field.getId());

        // Recupera il campo "field" come una mappa
        Map<String, Object> newFieldMap = (Map<String, Object>) requestBody.get("field");

        if (newFieldMap != null) {            
            JSONObject newField = new JSONObject(newFieldMap);

            String name = newField.getString("name");

            if (name == null || name.trim().length() == 0) {
                response.put("message", "name non poò essere vuoto");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            fieldTypeString = newField.getString("type");
            fieldType = FieldType.valueOf(fieldTypeString.toUpperCase());
            parameter = convertParameter(newField, fieldType);

            field.setValues(parameter);

            FieldDefinition fieldDefinition = field.getFieldDefinition();

            fieldDefinition.setName(name);
            fieldDefinitionDAO.save(fieldDefinition);

            fieldDAO.save(field);
        } else {
            response.put("error", "Il campo 'field' è nullo o mancante");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("message", "Field aggiornato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // TODO rimuovi field e rimuovi attività
}

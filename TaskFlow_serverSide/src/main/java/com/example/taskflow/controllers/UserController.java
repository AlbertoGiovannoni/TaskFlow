
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
import com.example.taskflow.DomainModel.FieldPackage.DateData;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserInfoDAO userInfoDAO;

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

    private ResponseEntity<Map<String, String>> validateFields(Map<String, Object> requestBody) {
        String email = (String) requestBody.get("email");
        String password = (String) requestBody.get("password");
        String username = (String) requestBody.get("username");
        Object isAdminObj = requestBody.get("isAdmin"); // Extract isAdmin as an Object

        Map<String, String> response = new HashMap<>();

        if (email == null || email.trim().isEmpty()) {
            response.put("message", "Email cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (password == null || password.trim().isEmpty()) {
            response.put("message", "Password cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (username == null || username.trim().isEmpty()) {
            response.put("message", "Username cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (isAdminObj == null || !(isAdminObj instanceof Boolean)) {
            response.put("message", "isAdmin must be a non-null boolean");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return null; // Nessun errore trovato
    }

    // ------------------------------ USER ---------------------------------- //

    @PostMapping("/public/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, Object> requestBody) {
        Map<String, String> response = new HashMap<>();

        ResponseEntity<Map<String, String>> validationResponse = validateFields(requestBody);

        if (validationResponse != null) {
            return validationResponse;
        }

        String email = (String) requestBody.get("email");
        String password = (String) requestBody.get("password");
        String username = (String) requestBody.get("username");
        boolean isAdmin = Boolean.valueOf((boolean) requestBody.get("isAdmin"));

        // Controllo se l'email è già utilizzata
        if (userInfoDAO.findByEmail(email).isPresent()) {
            response.put("error", "Email già esistente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        // Controllo se l'username è già utilizzata
        if (userDAO.findByUsername(username).isPresent()) {
            response.put("error", "Username già esistente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // String hashedPassword = passwordEncoder.encode(password);

        UserInfo userInfo = new UserInfo(email, password);
        userInfoDAO.save(userInfo);
        User user = new User(userInfo, username, isAdmin);
        userDAO.save(user);

        response.put("message", "User creato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/user/updateUser")
    public ResponseEntity<Map<String, String>> updateUser(@RequestBody Map<String, Object> requestBody) {

        Map<String, String> response = new HashMap<>();
        String userId = (String) requestBody.get("userId");

        if (userId == null || userId.trim().isEmpty()) {
            response.put("message", "userId cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userDAO.findById(userId);

        if (optionalUser.isPresent()) {

            ResponseEntity<Map<String, String>> validationResponse = validateFields(requestBody);

            if (validationResponse != null) {
                return validationResponse;
            }

            User user = optionalUser.get();
            UserInfo userInfo = user.getUserInfo();

            userInfo.setEmail((String) requestBody.get("email"));
            userInfo.setPassword((String) requestBody.get("password"));
            user.setUsername((String) requestBody.get("username"));

            userInfoDAO.save(userInfo);
            userDAO.save(user);
        } else {
            response.put("message", "User not found");
        }

        response.put("message", "User aggiornato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ------------------------------ ACTIVITY ---------------------------------- //

    private ArrayList<?> convertParameter(JSONObject parameter, FieldType type) {

        switch (type) {
            case ASSIGNEE:
                ArrayList<User> userPar = new ArrayList<>();

                JSONArray userIds = parameter.getJSONArray("value");

                for (int j = 0; j < userIds.length(); j++) {
                    String userId = userIds.getString(j);

                    // Check if the user ID is valid
                    if (userId == null || userId.trim().isEmpty()) {
                        throw new IllegalArgumentException("User ID cannot be null or empty.");
                    }

                    try {
                        // Attempt to find the user by ID using the userDAO
                        Optional<User> tmp = userDAO.findById(userId);

                        // Check if the user is found
                        if (tmp.isPresent()) {
                            userPar.add(tmp.get());
                        } else {
                            throw new IllegalArgumentException("User with ID " + userId + " not found.");
                        }

                    } catch (Exception e) {
                        System.err.println("Error fetching user with ID " + userId + ": " + e.getMessage());
                        throw e;
                    }

                }

                return userPar;

            case SINGLE_SELECTION:
                ArrayList<String> stringPar = new ArrayList<>();
                JSONObject paramObject = parameter;

                if (paramObject.has("value") && paramObject.get("value") instanceof JSONArray) {
                    JSONArray valueArray = paramObject.getJSONArray("value");

                    for (int j = 0; j < valueArray.length(); j++) {
                        stringPar.add(valueArray.getString(j));
                    }
                } else {

                    throw new IllegalArgumentException("Expected 'value' to be a JSON array of strings.");
                }

                return stringPar;

            case DATE:

                ArrayList<DateData> datePar = new ArrayList<DateData>();

                JSONObject obj = parameter;
                String dateString = obj.getString("value");

                // creazione notifica

                if (obj.has("notification")) {
                    Object notif = obj.get("notification");

                    JSONObject notificationJson = new JSONObject(notif.toString());

                    ArrayList<User> receivers = new ArrayList<>();

                    JSONArray userIdsR = notificationJson.getJSONArray("receivers");

                    for (int j = 0; j < userIdsR.length(); j++) {
                        String userId = userIdsR.getString(j);

                        // Check if the user ID is valid
                        if (userId == null || userId.trim().isEmpty()) {
                            throw new IllegalArgumentException("User ID cannot be null or empty.");
                        }

                        try {
                            // Attempt to find the user by ID using the userDAO
                            Optional<User> tmp = userDAO.findById(userId);

                            // Check if the user is found
                            if (tmp.isPresent()) {
                                receivers.add(tmp.get());
                            } else {
                                throw new IllegalArgumentException("User with ID " + userId + " not found.");
                            }

                        } catch (Exception e) {
                            System.err.println("Error fetching user with ID " + userId + ": " + e.getMessage());
                            throw e;
                        }
                    }

                    LocalDateTime date = LocalDateTime.parse(dateString);
                    Integer advanceHours = notificationJson.getInt("advanceHours");
                    Notification notification = new Notification(receivers, date.minusHours(advanceHours),
                            notificationJson.getString("message"));
                    this.notificationDAO.save(notification);

                    DateData dateData = new DateData(date, notification);
                    datePar.add(dateData);

                } else {
                    LocalDateTime date = LocalDateTime.parse(dateString);
                    DateData dateData = new DateData(date);
                    datePar.add(dateData);

                }
                // datePar.add(dateData);

                return datePar;

            case NUMBER:

                ArrayList<Float> numberPar = new ArrayList<Float>();
                numberPar.add(Float.parseFloat(parameter.getString("value")));
                return numberPar;

            // case DOCUMENT: //TODO
            // return new SimpleFieldDefinitionBuilder(type);
            case TEXT:
                ArrayList<String> textPar = new ArrayList<String>();
                textPar.add(parameter.getString("value"));
                return textPar;

            default:
                throw new IllegalArgumentException("Unsopported type: " + type);
        }

    }

    @PostMapping("/user/createActivity")
    public ResponseEntity<Map<String, String>> createNewActivity(@RequestBody Map<String, Object> requestBody) {

        Map<String, String> response = new HashMap<>();

        String name = (String) requestBody.get("activityName");
        Object fieldsObject = requestBody.get("activityFields");

        // Controllo e conversione dell'input
        if (name == null || fieldsObject == null || name.trim().isEmpty()) {
            response.put("message", "activityName or activityFields cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verifica che activityFields sia un array JSON
        if (!(fieldsObject instanceof ArrayList)) {
            response.put("message", "activityFields must be a JSON array");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        JSONArray fieldsJson = new JSONArray((ArrayList<?>) fieldsObject);
        ArrayList<Field> fields = new ArrayList<>();

        String fieldTypeString;
        FieldType fieldType;
        ArrayList<?> parameter;

        JSONObject fieldJson = new JSONObject();
        for (int i = 0; i < fieldsJson.length(); i++) {
            fieldJson = fieldsJson.getJSONObject(i);
            fieldTypeString = fieldJson.getString("type");
            fieldType = FieldType.valueOf(fieldTypeString.toUpperCase());

            FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(fieldType)
                    .setName(fieldJson.getString("name"))
                    .build();

            parameter = convertParameter(fieldsJson.getJSONObject(i), fieldType);

            if (fieldType == fieldType.ASSIGNEE || fieldType == fieldType.SINGLE_SELECTION) {
                fieldDefinition.addMultipleEntry(parameter); // TODO restituire fieldDefinition dal metodo di parameter?
            }

            fieldDefinitionDAO.save(fieldDefinition);

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

    @PostMapping("user/addField") // TODO fare check anche per name type e value che siano non nulli sia qui che
                                  // in create activity??
    public ResponseEntity<Map<String, String>> addField(@RequestBody Map<String, Object> requestBody) {
        Map<String, String> response = new HashMap<>();

        String activityId = (String) requestBody.get("activityId");
        Object fieldObject = requestBody.get("field");

        if (activityId == null || activityId.trim().isEmpty()) {
            response.put("message", "activityId cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Controllo che field sia un JSON valido
        if (!(fieldObject instanceof Map)) {
            response.put("message", "field must be a JSON object");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Se field è un JSON valido, convertilo in JSONObject
        JSONObject fieldJson;
        try {
            fieldJson = new JSONObject((Map<?, ?>) fieldObject);
        } catch (JSONException e) {
            response.put("message", "field must be a valid JSON object");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String fieldTypeString;
        FieldType fieldType;
        ArrayList<?> parameter;

        fieldTypeString = fieldJson.getString("type");
        fieldType = FieldType.valueOf(fieldTypeString.toUpperCase());

        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(fieldType)
                .setName(fieldJson.getString("name"))
                .build();

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

        Activity activity = activityDAO.findById(activityId).orElse(null);
        activity.addField(field);

        activityDAO.save(activity);

        response.put("message", "Field aggiunto");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/user/updateActivity") // TODO rendi PATCH
    public ResponseEntity<Map<String, String>> updateActivity(@RequestBody Map<String, Object> requestBody) {

        Map<String, String> response = new HashMap<>();
        String activityId = (String) requestBody.get("activityId");

        if (activityId == null || activityId.trim().isEmpty()) {
            response.put("message", "activityId cannot be empty");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<Activity> optionalActivity = activityDAO.findById(activityId);

        if (optionalActivity.isPresent()) {

            ResponseEntity<Map<String, String>> validationResponse = validateFields(requestBody);

            if (validationResponse != null) {
                return validationResponse;
            }

            Activity activity = optionalActivity.get();

            // TODO modifica activity (addField in query separata?)

            // TODo come facciamo update? passiamo tutti i field e li ricreiamo per
            // applicare le modifiche o facciamo api di modifica field

            activity.setName((String) requestBody.get("name"));

            activityDAO.save(activity);
        } else {
            response.put("message", "User not found");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        response.put("message", "User aggiornato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/user/updateField") // TODO PATCH + da testare
    public ResponseEntity<Map<String, String>> updateField(@RequestBody Map<String, Object> requestBody) {
        Map<String, String> response = new HashMap<>();

        String fieldId = (String) requestBody.get("fieldId");
        Optional<Field> optionalField = fieldDAO.findById(fieldId);
        ArrayList<?> parameter;
        FieldType fieldType;
        String fieldTypeString;

        if (optionalField.isPresent()) {
            Field field = optionalField.get();

            System.out.println(field.getId());

            // Recupera il campo "field" come una mappa
            Map<String, Object> newFieldMap = (Map<String, Object>) requestBody.get("field");

            if (newFieldMap != null) {
                // Se vuoi un JSONObject, converti la mappa in un JSONObject
                JSONObject newField = new JSONObject(newFieldMap);

                fieldTypeString = newField.getString("type");
                fieldType = FieldType.valueOf(fieldTypeString.toUpperCase());
                parameter = convertParameter(newField, fieldType);

                field.setValues(parameter);

                FieldDefinition fieldDefinition = field.getFieldDefinition();

                fieldDefinition.setName(newField.getString("name"));
                fieldDefinitionDAO.save(fieldDefinition);

                fieldDAO.save(field);
            } else {
                response.put("error", "Il campo 'field' è nullo o mancante");
                return ResponseEntity.badRequest().body(response);
            }
        } else {
            response.put("error", "Field non trovato");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("message", "Field aggiornato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}

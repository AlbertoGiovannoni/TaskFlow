
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
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DomainModel.Activity;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserInfoDAO userInfoDAO;

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

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);

        UserInfo userInfo = new UserInfo(email, hashedPassword);
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

                // Return the list of User objects
                return userPar;

            case SINGLE_SELECTION:
                ArrayList<String> stringPar = new ArrayList<>();

                // Assuming 'parameters' is a JSONArray
                // Accessing the JSON object at the current index
                JSONObject paramObject = parameter;

                // Checking if "value" is a JSON array
                if (paramObject.has("value") && paramObject.get("value") instanceof JSONArray) {
                    JSONArray valueArray = paramObject.getJSONArray("value");

                    // Iterating over the array to add each string to stringPar
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

                DateData dateData = new DateData(LocalDateTime.parse(dateString));
                datePar.add(dateData);

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

            if (fieldType == fieldType.ASSIGNEE) {
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

    @PostMapping("/user/updateActivity")
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

            //TODO modifica activity (addField in query separata?)

            // userInfo.setEmail((String) requestBody.get("email"));
            // userInfo.setPassword((String) requestBody.get("password"));
            // user.setUsername((String) requestBody.get("username"));

            activityDAO.save(activity);
        } else {
            response.put("message", "User not found");
        }

        response.put("message", "User aggiornato");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
}
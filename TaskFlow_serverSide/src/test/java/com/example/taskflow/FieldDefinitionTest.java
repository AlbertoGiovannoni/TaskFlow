package com.example.taskflow;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.AssigneeDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SimpleFieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SingleSelectionDefinitionBuilder;

import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

@DataMongoTest
@ActiveProfiles("test")
public class FieldDefinitionTest {

    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    MongoTemplate template;

    ArrayList<User> someUsers;
    ArrayList<String> someSingleSelections;

    @BeforeEach
    public void setupDatabase(){
        if (template.collectionExists("fieldDefinition")){
            template.dropCollection("fieldDefinition");
        }
        if (template.collectionExists("user")){
            template.dropCollection("user");
        }
        if (template.collectionExists("userInfo")){
            template.dropCollection("userInfo");
        }

        for (int i = 0; i < 5; i++){
            this.addGetRandomUserToDatabase();
        }

        this.someUsers = this.userDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));

        this.someSingleSelections = new ArrayList<>();
        this.someSingleSelections.add("Done");
        this.someSingleSelections.add("In progress");
        this.someSingleSelections.add("Waiting");
    }

    @Test
    public void testInsertAndFindSimpleFieldDefinition() {
        FieldDefinition fieldDefinition = new SimpleFieldDefinitionBuilder(FieldType.DATE)
                                    .setName("Data scadenza")
                                    .build();

        this.fieldDefinitionDAO.save(fieldDefinition);

        FieldDefinition fieldDefinitionFromDB = fieldDefinitionDAO.findById(fieldDefinition.getId()).orElse(null);

        assertEquals(fieldDefinition, fieldDefinitionFromDB);
    }

    @Test
    public void testInsertAndFindAssigneeFieldDefinition() {
        FieldDefinition fieldDefinition = new AssigneeDefinitionBuilder()
                                    .setUsers(this.someUsers)
                                    .setName("Partecipanti")
                                    .build();

        this.fieldDefinitionDAO.save(fieldDefinition);

        FieldDefinition fieldDefinitionFromDB = fieldDefinitionDAO.findById(fieldDefinition.getId()).orElse(null);

        assertEquals(fieldDefinition, fieldDefinitionFromDB);
    }

    @Test
    public void testInsertAndFindSingleSelectionFieldDefinition() {
        FieldDefinition fieldDefinition = new SingleSelectionDefinitionBuilder()
                                    .setSelections(this.someSingleSelections)
                                    .setName("Status")
                                    .build();

        FieldDefinition fieldDefinitionFromDB = this.fieldDefinitionDAO.save(fieldDefinition);

        assertEquals(fieldDefinition, fieldDefinitionFromDB);
    }

    @Test
    public void testModifyAssigneeDefintion(){
        FieldDefinition fieldDefinition = new AssigneeDefinition(UUID.randomUUID().toString(), RandomString.make(10), someUsers);

        User newUserForAssignee = this.addGetRandomUserToDatabase();
        
        fieldDefinition.reset();
        String anotherName = RandomString.make(10);
        fieldDefinition.setName(anotherName);
        assertEquals(anotherName, fieldDefinition.getName());

        fieldDefinition.reset();
        fieldDefinition.addSingleEntry(newUserForAssignee);
        assertEquals(fieldDefinition.getSingleEntry(), newUserForAssignee);

        fieldDefinition.reset();
        ArrayList<User> someUsers = this.addGetMultipleRandomUserToDatabase(5);
        fieldDefinition.addMultipleEntry(someUsers);
        assertEquals(fieldDefinition.getAllEntries(), someUsers);

        FieldDefinition fieldDefinitionPushed = this.fieldDefinitionDAO.save(fieldDefinition);

        assertEquals(fieldDefinition.getId(), fieldDefinitionPushed.getId());
    }

    @Test
    public void testModifySimpleDefintion(){
        FieldDefinition fieldDefinition = this.pushGetRandomFieldDefinitionToDatabase(FieldType.TEXT);
        
        fieldDefinition.reset();
        String anotherName = RandomString.make(10);
        fieldDefinition.setName(anotherName);
        assertEquals(anotherName, fieldDefinition.getName());

        FieldDefinition fieldDefinitionPushed = this.fieldDefinitionDAO.save(fieldDefinition);

        assertEquals(fieldDefinition.getId(), fieldDefinitionPushed.getId());
    }

    private User addGetRandomUserToDatabase(){
        UserInfo info = new UserInfo(UUID.randomUUID().toString(), RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(info);

        User user = new User(UUID.randomUUID().toString(), info, RandomString.make(10));
        return this.userDAO.save(user);
    }

    private ArrayList<User> addGetMultipleRandomUserToDatabase(int n){
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < n; i++){
            users.add(this.addGetRandomUserToDatabase());
        }

        return users;
    }

    private FieldDefinition pushGetRandomFieldDefinitionToDatabase(FieldType type){
        FieldDefinition fieldDefinition = new SimpleFieldDefinitionBuilder(type)
                                    .setName(RandomString.make(10))
                                    .build();

        return this.fieldDefinitionDAO.save(fieldDefinition);
    }
}

package com.example.taskflow;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.AssigneeDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.mongodb.client.result.UpdateResult;

import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.expression.spel.ast.Assign;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;

import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;

import java.util.ArrayList;
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
    public void testInsertAndFindAssigneeFieldDefinition() {
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(FieldType.ASSIGNEE)
                                    .addCommonAttributes("Partecipanti")
                                    .setUsers(this.someUsers)
                                    .build();

        this.fieldDefinitionDAO.save(fieldDefinition);

        FieldDefinition fieldDefinitionFromDB = fieldDefinitionDAO.findById(fieldDefinition.getId()).orElse(null);

        assertEquals(fieldDefinition, fieldDefinitionFromDB);
    }

    @Test
    public void testInsertAndFindSingleSelectionFieldDefinition() {
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(FieldType.SINGLE_SELECTION)
                                    .addCommonAttributes("Status")
                                    .setString(this.someSingleSelections)
                                    .build();

        FieldDefinition fieldDefinitionFromDB = this.fieldDefinitionDAO.save(fieldDefinition);

        assertEquals(fieldDefinition, fieldDefinitionFromDB);
    }
    
    @Test
    public void updateFieldDefinitionWithObject(){
        FieldDefinition fieldDefinition = this.pushGetRandomFieldDefinitionToDatabase(FieldType.ASSIGNEE);

        User newUserForAssignee = this.addGetRandomUserToDatabase();
        
        fieldDefinition.addUser(newUserForAssignee);

        FieldDefinition fieldDefinitionPushed = this.fieldDefinitionDAO.save(fieldDefinition);

        assertEquals(fieldDefinition.getId(), fieldDefinitionPushed.getId());
    }

    private User addGetRandomUserToDatabase(){
        UserInfo info = new UserInfo(RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(info);

        User user = new User(info, RandomString.make(10));
        return this.userDAO.save(user);
    }

    private FieldDefinition pushGetRandomFieldDefinitionToDatabase(FieldType type){
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(type)
                                    .addCommonAttributes(RandomString.make(10))
                                    .setUsers(this.someUsers)
                                    .build();

        return this.fieldDefinitionDAO.save(fieldDefinition);
    }
}

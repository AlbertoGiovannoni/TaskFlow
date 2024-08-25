package com.example.taskflow;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinitionFactory;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssegneeDefinitionFactory;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;

import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    ArrayList<String> someSingleSeletions;

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
            this.addRandomUserToDatabase();
        }

        this.someUsers = this.userDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));

        someSingleSeletions = new ArrayList<String>();
        someSingleSeletions.add("Done");
        someSingleSeletions.add("In progress");
        someSingleSeletions.add("Waiting");
    }

    private void addRandomUserToDatabase(){
        UserInfo info = new UserInfo(RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(info);

        User user = new User(info, RandomString.make(10));
        this.userDAO.save(user);
    }

    @Test
    public void testInsertAndFindFieldDefinition() {
        
        FieldDefinition fieldDefinition = new SimpleFieldDefinitionFactory()
                .addCommonAttributes("scadenza", FieldType.DATE)
                .build();

        fieldDefinition = fieldDefinitionDAO.save(fieldDefinition);

        FieldDefinition found = fieldDefinitionDAO.findById(fieldDefinition.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(fieldDefinition.getName(), found.getName());
    }

    @Test
    public void testAssigneeDefinition(){

        FieldDefinition fieldDefinition = new AssegneeDefinitionFactory()
                .addCommonAttributes("reviewers", FieldType.ASSIGNEE)
                .addSpecificField(this.someUsers)
                .build();    

        fieldDefinitionDAO.save(fieldDefinition);
    }

    public void singleSelectionDefinition(){

        FieldDefinition fieldDefinition = new SingleSelectionDefinitionFactory()
                .addCommonAttributes("reviewers", FieldType.ASSIGNEE)
                .addSpecificField(someSingleSeletions)
                .build();    

        fieldDefinitionDAO.save(fieldDefinition);
    }
    
}

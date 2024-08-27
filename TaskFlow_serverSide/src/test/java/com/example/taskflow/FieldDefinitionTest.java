package com.example.taskflow;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.AssigneeDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;

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
            this.addRandomUserToDatabase();
        }

        this.someUsers = this.userDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));

        someSingleSelections = new ArrayList<>();
        someSingleSelections.add("Done");
        someSingleSelections.add("In progress");
        someSingleSelections.add("Waiting");
    }

    private void addRandomUserToDatabase(){
        UserInfo info = new UserInfo(RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(info);

        User user = new User(info, RandomString.make(10));
        this.userDAO.save(user);
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
    
}

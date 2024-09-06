package com.example.taskflow;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.DateData;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;


import net.bytebuddy.utility.RandomString;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class ActivityTest {
    @Autowired
    private TestUtil testUtil;

    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    private NotificationDAO notificationDao;
    @Autowired
    private UserDAO userDao;

    @Autowired
    private ActivityDAO activityDAO;

    @Autowired
    private MongoTemplate template;

    @BeforeEach
    public void setupDatabase() {
        // this.testUtil.cleanDatabase();
    }

    @Test
    public void testInsertAndFindActivity() {
        ArrayList<Field> fields = new ArrayList<Field>();

        FieldDefinition fieldDefinition = this.testUtil.pushGetRandomFieldDefinitionToDatabase(FieldType.ASSIGNEE);

        ArrayList<User> someUsers = this.testUtil.addGetMultipleRandomUserToDatabase(5);

        fieldDefinition.addMultipleEntry(someUsers);
        this.fieldDefinitionDao.save(fieldDefinition);

        Field field = FieldFactory.getBuilder(FieldType.ASSIGNEE)
                .addFieldDefinition(fieldDefinition)
                .addParameter(someUsers.get(0))
                .build();

        field = this.fieldDao.save(field);

        fields.add(field);

        Activity activity = new Activity(RandomString.make(10), fields);
        activityDAO.save(activity);
    }

    @Test
    public void testModifyActivity() {
        ArrayList<Field> fields = new ArrayList<Field>();

        FieldDefinition fieldDefinition = this.testUtil.pushGetRandomFieldDefinitionToDatabase(FieldType.ASSIGNEE);

        ArrayList<User> someUsers = this.testUtil.addGetMultipleRandomUserToDatabase(5);

        fieldDefinition.addMultipleEntry(someUsers);
        this.fieldDefinitionDao.save(fieldDefinition);

        Field field = FieldFactory.getBuilder(FieldType.ASSIGNEE)
                .addFieldDefinition(fieldDefinition)
                .addParameter(someUsers.get(0))
                .build();

        Field fieldFromDb = this.fieldDao.save(field);

        User newUser = this.testUtil.addGetRandomUserToDatabase();

        fieldDefinition.addSingleEntry(newUser);
        this.fieldDefinitionDao.save(fieldDefinition);

        fieldFromDb.addValue(newUser);

        boolean found = false;
        ArrayList<?> users = fieldFromDb.getValues();
        for (Object user : users) {
            if (user instanceof User) {
                if (user.equals(newUser)) {
                    found = true;
                }
            }
        }
        assertTrue(found);
    }

}

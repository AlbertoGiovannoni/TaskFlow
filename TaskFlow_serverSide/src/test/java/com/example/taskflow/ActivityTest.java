package com.example.taskflow;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
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

    private Activity activity;

    @Autowired
    private ActivityDAO activityDAO;

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
        ArrayList<Field> fields = new ArrayList<Field>();

        FieldDefinition fieldDefinition1 = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.TEXT);
        FieldDefinition fieldDefinition2 = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.NUMBER);

        ArrayList<User> someUsers = this.testUtil.addGetMultipleRandomUserToDatabase(5);

        this.fieldDefinitionDao.save(fieldDefinition1);
        this.fieldDefinitionDao.save(fieldDefinition2);

        Field field1 = FieldFactory.getBuilder(FieldType.TEXT)
                .addFieldDefinition(fieldDefinition1)
                .addParameter("diocan")
                .build();

        Float value = new Float("2");
        Field field2 = FieldFactory.getBuilder(FieldType.NUMBER)
                .addFieldDefinition(fieldDefinition2)
                .addParameter(value)
                .build();

        field1 = this.fieldDao.save(field1);
        field2 = this.fieldDao.save(field2);

        fields.add(field1);
        fields.add(field2);

        this.activity = new Activity(RandomString.make(10), fields);
        activityDAO.save(this.activity);
    }

    @Test
    public void testInsertAndFindActivity() {
        Activity found = activityDAO.findById(activity.getId()).orElse(null);
        assertNotNull(found);
        this.testUtil.checkEqualActivities(activity, found);
    }

    @Test
    public void modifyActivity(){
        FieldDefinition fieldDefinition1 = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.TEXT);
        FieldDefinition fieldDefinition2 = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.NUMBER);

        Field field1 = FieldFactory.getBuilder(FieldType.TEXT)
                .addFieldDefinition(fieldDefinition1)
                .addParameter("foo")
                .build();

        Float value = new Float("2");
        Field field2 = FieldFactory.getBuilder(FieldType.NUMBER)
                .addFieldDefinition(fieldDefinition2)
                .addParameter(value)
                .build();

        ArrayList<Field> fields = new ArrayList<Field>();
        fields.add(field1);
        fields.add(field2);

        field1 = this.fieldDao.save(field1);
        field2 = this.fieldDao.save(field2);
        Activity found1 = activityDAO.findById(this.activity.getId()).orElse(null);
        found1.setFields(fields);
        activityDAO.save(found1);
        Activity found2 = activityDAO.findById(activity.getId()).orElse(null);
        this.testUtil.checkEqualActivities(found1, found2);
    }

    public void test(){

    }
}

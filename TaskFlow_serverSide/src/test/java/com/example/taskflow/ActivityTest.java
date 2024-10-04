package com.example.taskflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.NumberBuilder;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.TextBuilder;

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
    private ArrayList<Field> someFields;

    @Autowired
    private ActivityDAO activityDAO;

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
        ArrayList<Field> fields = new ArrayList<Field>();

        FieldDefinition fieldDefinition1 = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.TEXT);
        FieldDefinition fieldDefinition2 = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.NUMBER);

        this.fieldDefinitionDao.save(fieldDefinition1);
        this.fieldDefinitionDao.save(fieldDefinition2);

        Field field1 = (new TextBuilder(fieldDefinition1))
                .addText("diocan")
                .build();

        Field field2 = (new NumberBuilder(fieldDefinition2))
                .addParameter(BigDecimal.valueOf(Math.random()))
                .build();

        field1 = this.fieldDao.save(field1);
        field2 = this.fieldDao.save(field2);

        fields.add(field1);
        fields.add(field2);

        this.activity = new Activity(UUID.randomUUID().toString(), RandomString.make(10), fields);
        activityDAO.save(this.activity);

        this.someFields = fields;
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

        
        Field field1 = (new TextBuilder(fieldDefinition1))
                .addText("foo")
                .build();

        Field field2 = (new NumberBuilder(fieldDefinition2))
                .addParameter(BigDecimal.valueOf(Math.random()))
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

    @Test
    public void testFieldInActivity(){
        Activity activityFromDatabase = this.activityDAO.findById(this.activity.getId()).orElse(null);

        assertEquals(activityFromDatabase.getFields().get(0), this.someFields.get(0));
        assertEquals(activityFromDatabase.getFields().get(1), this.someFields.get(1));
    }
}

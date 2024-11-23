package com.unifi.taskflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.unifi.taskflow.daos.ActivityDAO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.domainModel.Activity;
import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.NumberBuilder;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.TextBuilder;

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
                .addText("Test")
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
    public void testFieldMethods(){
        // creazione di field casuali
        FieldDefinition fieldDef1 = this.testUtil.getFieldDefinition(FieldType.TEXT);
        FieldDefinition fieldDef2 = this.testUtil.getFieldDefinition(FieldType.NUMBER);
        Field field1 = this.testUtil.getField(fieldDef1);
        Field field2 = this.testUtil.getField(fieldDef2);

        // test su ordinamento dei fields
        this.activity.addField(field1);
        this.activity.addField(field2);
        assertEquals(this.activity.getFields().get(0), field1);
        assertEquals(this.activity.getFields().get(1), field2);

        // test di rimozione field
        this.activity.removeField(field1);
        this.activity.removeField(field2);
        assertTrue(this.activity.getFields().isEmpty());

        // test di aggiunta con metodo addFields()
        ArrayList<Field> fields = new ArrayList<Field>();
        fields.add(field1);
        fields.add(field2);
        this.activity.addFields(fields);
        assertEquals(this.activity.getFields().get(0), field1);
        assertEquals(this.activity.getFields().get(1), field2);
    }

    @Test
    public void testActivityFieldsModificationDB(){
        // creazione di field casuali e persistenza
        FieldDefinition fieldDefinition1 = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.TEXT);
        FieldDefinition fieldDefinition2 = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.NUMBER);
        Field field1 = this.testUtil.getField(fieldDefinition1);
        Field field2 = this.testUtil.getField(fieldDefinition2);
        field1 = this.fieldDao.save(field1);
        field2 = this.fieldDao.save(field2);

        // aggiunta dei field all'activity e persistenza
        this.activity.addField(field1);
        this.activity.addField(field2);
        this.activityDAO.save(this.activity);

        // test della persistenza dell'aggiunta dei fields
        Activity activitySearchedInDB = activityDAO.findById(this.activity.getId()).orElse(null);
        assertEquals(activitySearchedInDB.getFields().get(0), field1);
        assertEquals(activitySearchedInDB.getFields().get(1), field2);

        // rimozione di un field dall'activity e persistenza
        this.activity.removeField(field2);
        this.activityDAO.save(activity);
        
        // test della persistenza della rimozione del field
        activitySearchedInDB = activityDAO.findById(this.activity.getId()).orElse(null);
        assertEquals(this.activity.getFields().size(), 1);
        assertEquals(this.activity.getFields().get(0), field1);
    }

    @Test
    public void testFieldInActivity(){
        Activity activityFromDatabase = this.activityDAO.findById(this.activity.getId()).orElse(null);

        assertEquals(activityFromDatabase.getFields().get(0), this.someFields.get(0));
        assertEquals(activityFromDatabase.getFields().get(1), this.someFields.get(1));
    }
}

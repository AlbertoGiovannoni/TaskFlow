package com.example.taskflow;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;

import net.bytebuddy.utility.RandomString;


@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class FieldTest {
    @Autowired
    private TestUtil testUtil;

    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDao;

    @Autowired
    private MongoTemplate template;

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
    }

    @Test
    public void testTextField(){
        FieldDefinition fieldDefinition = this.testUtil.pushGetRandomFieldDefinitionToDatabase(FieldType.TEXT);

        Field field = FieldFactory.getBuilder(FieldType.TEXT)
                                .addFieldDefinition(fieldDefinition)
                                .addParameter(RandomString.make(10))
                                .build();

        Field fieldFromDB = this.fieldDao.save(field);

        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);

        String randomString = RandomString.make(10);
        fieldFromDB.setValue(randomString);

        Field fieldModified = this.fieldDao.save(fieldFromDB);

        assertEquals(fieldModified.getValue(), randomString);
        assertEquals(fieldModified.getUuid(), fieldFromDB.getUuid());
        assertEquals(field.getUuid(), fieldFromDB.getUuid());
    }

    @Test
    public void testNumberField(){
        FieldDefinition fieldDefinition = this.testUtil.pushGetRandomFieldDefinitionToDatabase(FieldType.NUMBER);

        Field field = FieldFactory.getBuilder(FieldType.NUMBER)
                                .addFieldDefinition(fieldDefinition)
                                .addParameter((float)Math.random())
                                .build();

        Field fieldFromDB = this.fieldDao.save(field);

        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);

        Float randomFloat = (float)Math.random();
        fieldFromDB.setValue(randomFloat);

        Field fieldModified = this.fieldDao.save(fieldFromDB);

        assertEquals(fieldModified.getValue(), randomFloat);
        assertEquals(fieldModified.getUuid(), fieldFromDB.getUuid());
        assertEquals(field.getUuid(), fieldFromDB.getUuid());
    }

    @Test
    public void testSingleSelectionField(){
        FieldDefinition fieldDefinition = this.testUtil.pushGetRandomFieldDefinitionToDatabase(FieldType.SINGLE_SELECTION);

        ArrayList<String> someSelections = new ArrayList<>();
        ArrayList<String> subsetOfSomeSelections = new ArrayList<>();
        ArrayList<String> someSelectionsNotValid = new ArrayList<>();

        String randomString = RandomString.make(10);

        for (int i = 0; i < 10; i++){
            someSelections.add(randomString);
            someSelectionsNotValid.add(RandomString.make(10));
            if (i % 2 == 0){
                subsetOfSomeSelections.add(randomString);
            }
        }

        fieldDefinition.addMultipleEntry(someSelections);
        this.fieldDefinitionDao.save(fieldDefinition);

        Field field = FieldFactory.getBuilder(FieldType.SINGLE_SELECTION)
                                .addFieldDefinition(fieldDefinition)
                                .addParameter(someSelections.get(1))
                                .build();

        Field fieldFromDB = this.fieldDao.save(field);

        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);

        fieldFromDB.setValues(subsetOfSomeSelections);

        assertEquals(field, fieldFromDB);
        assertEquals(fieldFromDB.getFieldDefinition(), fieldDefinition);

        int i = 0;
        for (Object value : fieldFromDB.getValues()){
            assertEquals((String)value, subsetOfSomeSelections.get(i));
            i++;
        }
        assertEquals(field, this.fieldDao.save(fieldFromDB));

        assertThrows(IllegalArgumentException.class, ()->{fieldFromDB.addValue("a string not accepted");});
        assertThrows(IllegalArgumentException.class, ()->{fieldFromDB.addValues(someSelectionsNotValid);});
        assertThrows(IllegalArgumentException.class, ()->{fieldFromDB.setValues(someSelectionsNotValid);});
    }

    @Test
    public void testAssigneeField(){
        FieldDefinition fieldDefinition = this.testUtil.pushGetRandomFieldDefinitionToDatabase(FieldType.ASSIGNEE);

        ArrayList<User> someUsers = this.testUtil.addGetMultipleRandomUserToDatabase(5);
        ArrayList<User> subsetOfSomeUsers = new ArrayList<>(Arrays.asList(someUsers.get(0), someUsers.get(3)));

        fieldDefinition.addMultipleEntry(someUsers);
        this.fieldDefinitionDao.save(fieldDefinition);

        Field field = FieldFactory.getBuilder(FieldType.ASSIGNEE)
                                .addFieldDefinition(fieldDefinition)
                                .addParameter(someUsers.get(0))
                                .build();

        Field fieldFromDB = this.fieldDao.save(field);

        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);

        fieldFromDB.setValues(subsetOfSomeUsers);
        assertEquals(field, fieldFromDB);

        fieldFromDB.reset();
        assertEquals(0, fieldFromDB.getValues().size());

        assertThrows(IllegalArgumentException.class, ()->{fieldFromDB.addValue(this.testUtil.addGetRandomUserToDatabase());});
        assertThrows(IllegalArgumentException.class, ()->{fieldFromDB.addValues(this.testUtil.addGetMultipleRandomUserToDatabase(3));});
        assertThrows(IllegalArgumentException.class, ()->{fieldFromDB.setValues((this.testUtil.addGetMultipleRandomUserToDatabase(3)));});
    }
}

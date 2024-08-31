package com.example.taskflow;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import java.util.random.RandomGeneratorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
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
    public void testAddingNewTextField(){
        FieldDefinition fieldDefinition = this.testUtil.pushGetRandomFieldDefinitionToDatabase(FieldType.TEXT);

        Field field = FieldFactory.getBuilder(FieldType.TEXT)
                                .addFieldDefinition(fieldDefinition)
                                .addParameter(RandomString.make(10))
                                .build();

        Field fieldFromDB = this.fieldDao.save(field);

        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);
    }

    @Test
    public void testAddingNewNumberField(){
        FieldDefinition fieldDefinition = this.testUtil.pushGetRandomFieldDefinitionToDatabase(FieldType.NUMBER);

        Field field = FieldFactory.getBuilder(FieldType.NUMBER)
                                .addFieldDefinition(fieldDefinition)
                                .addParameter((float)Math.random())
                                .build();

        Field fieldFromDB = this.fieldDao.save(field);

        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);
    }
}

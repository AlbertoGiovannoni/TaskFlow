package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.service.FieldDefinitionService;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class FieldDefinitionServiceTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private FieldDefinitionService fieldDefinitionService;

    @Autowired 
    private FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    private FieldDAO fieldDao;

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
    }

    @Test
    public void testDelete(){
        ArrayList<Field> fieldsDB = this.testUtil.pushGetNumberFieldsWithSameDefinition(10);
        
        FieldDefinition fieldDefinitionDB = fieldsDB.get(0).getFieldDefinition();
        
        this.fieldDefinitionService.delete(fieldDefinitionDB.getId());

        assertFalse(this.fieldDefinitionDao.existsById(fieldDefinitionDB.getId()));

        for (Field field : fieldsDB){
            assertFalse(this.fieldDao.existsById(field.getId()));
        }
    }
}

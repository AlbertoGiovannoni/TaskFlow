package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionService;

import net.bytebuddy.utility.RandomString;

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

    @Test
    public void testSimpleSave(){
        String name = RandomString.make(10);
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(FieldType.NUMBER)
                                                        .setName(name)
                                                        .build();
        FieldDefinition fieldDefinitionFromDB = this.fieldDefinitionService.createFieldDefinition(fieldDefinition);

        assertEquals(fieldDefinition, fieldDefinitionFromDB);
    }

    @Test
    public void testSaveFieldDefinitionByNameAndType(){
        String name;
        FieldDefinition fieldDefinition;
        
        for (FieldType type : FieldType.values()){
            name = RandomString.make(10);
            fieldDefinition = this.fieldDefinitionService.createFieldDefinition(type, name);
            assertEquals(fieldDefinition.getName(), name);
            assertEquals(fieldDefinition.getType(), type);
        }
    }

    @Test
    public void testSaveFieldDefinitionByNameAndTypeAndParameters(){
        String name;
        FieldDefinition fieldDefinition;
        
        name = RandomString.make(10);
        
        ArrayList<User> users = this.testUtil.addGetMultipleRandomUserToDatabase(10);
        
        fieldDefinition = this.fieldDefinitionService.createFieldDefinition(FieldType.ASSIGNEE, name, users);
        
        assertEquals(fieldDefinition.getName(), name);
        assertEquals(fieldDefinition.getType(), FieldType.ASSIGNEE);
        for (int i = 0; i < users.size(); i++){
            assertEquals(users.get(i), fieldDefinition.getAllEntries().get(i));
        }
    }
}

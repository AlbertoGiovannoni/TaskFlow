package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.SingleSelection;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.Mappers.FieldMapper;
import com.example.taskflow.service.FieldService.FieldServiceManager;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DTOs.Field.StringDTO;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class FieldServiceTest {

    @Autowired
    private TestUtil testUtil;
    @Autowired
    private FieldServiceManager fieldServiceManager;
    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    private UserDAO userDAO;

    private ArrayList<User> someUsers = new ArrayList<User>();

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
        for (int i = 0; i < 5; i++){
            this.testUtil.addGetRandomUserToDatabase();
        }
        this.someUsers = this.userDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));

    }

    @Test
    public void testCreationAssignee() {
        FieldDTO fieldDto = new AssigneeDTO();

        fieldDto.setType(FieldType.ASSIGNEE);
        FieldDefinition fd = FieldDefinitionFactory.getBuilder(FieldType.ASSIGNEE)
                .setName("meet")
                .addParameters(this.someUsers)
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());
        
        someUsers.remove(0);
        fieldDto.setValuesDto(extractIds(someUsers));

        FieldDTO createdFieldDto = fieldServiceManager.getFieldService(fieldDto).createField(fieldDto);

        Field createdField = this.fieldMapper.toEntity(createdFieldDto);

        Assignee found = (Assignee) this.fieldDao.findById(createdFieldDto.getId()).orElse(null);

        assertEquals(createdField, found);
    }

    private ArrayList<String> extractIds(ArrayList<User> users) {

        ArrayList<String> ids = new ArrayList<String>();
        for (User user : users){
            ids.add(user.getId());
        }

        return ids;
    }

    @Test
    public void testCreationText() {
        FieldDTO fieldDto = new StringDTO();

        fieldDto.setType(FieldType.TEXT);
        FieldDefinition fd = FieldDefinitionFactory.getBuilder(fieldDto.getType())
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());

        ArrayList<String> v = new ArrayList<String>();
        v.add("test");
        fieldDto.setValuesDto(v);

        FieldDTO createdFieldDto = fieldServiceManager.getFieldService(fieldDto).createField(fieldDto);

        Field createdField = this.fieldMapper.toEntity(createdFieldDto);
        FieldDefinition foundFD = fieldDefinitionDao.findById(createdFieldDto.getFieldDefinitionId()).orElse(null);
        
        
        createdField.setFieldDefinition(foundFD);

        Text found = (Text) this.fieldDao.findById(createdFieldDto.getId()).orElse(null);

        assertEquals(createdField, found);
    }

    @Test
    public void testCreationSingleSelection() {
        FieldDTO fieldDto = new StringDTO();

        fieldDto.setType(FieldType.SINGLE_SELECTION);
        ArrayList<String> selections = new ArrayList<String>();
        selections.add("Ready");
        selections.add("In progress");
        selections.add("Done");
        FieldDefinition fd = FieldDefinitionFactory.getBuilder(fieldDto.getType())
                .setName("meet")
                .addParameters(selections)
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());

        ArrayList<String> v = new ArrayList<String>();
        v.add("Reawcwdy"); //TODO non viene fatto il controllo se questo è un valore di fieldDefinition, si potrebbe fare qui direttamente o nel nuovo metodo di singleselection per assegnare la selection
        fieldDto.setValuesDto(v);

        FieldDTO createdFieldDto = fieldServiceManager.getFieldService(fieldDto).createField(fieldDto);
        Field createdField = this.fieldMapper.toEntity(createdFieldDto);
        FieldDefinition foundFD = fieldDefinitionDao.findById(createdFieldDto.getFieldDefinitionId()).orElse(null);
        
        
        createdField.setFieldDefinition(foundFD);

        SingleSelection found = (SingleSelection) this.fieldDao.findById(createdFieldDto.getId()).orElse(null);

        assertEquals(createdField, found);
    }


    @Test
    public void testCreationNumber() {
        FieldDTO fieldDto = new NumberDTO();

        fieldDto.setType(FieldType.NUMBER);
        FieldDefinition fd = FieldDefinitionFactory.getBuilder(fieldDto.getType())
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());

        ArrayList<String> v = new ArrayList<String>();
        v.add("8.2");
        fieldDto.setValuesDto(v);

        FieldDTO createdFieldDto = fieldServiceManager.getFieldService(fieldDto).createField(fieldDto);

        Field createdField = this.fieldMapper.toEntity(createdFieldDto);
        FieldDefinition foundFD = fieldDefinitionDao.findById(createdFieldDto.getFieldDefinitionId()).orElse(null);
        
        
        createdField.setFieldDefinition(foundFD);

        Field found = this.fieldDao.findById(createdFieldDto.getId()).orElse(null);

        assertEquals(createdField, found);
    }

}

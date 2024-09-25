package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.AssigneeDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SimpleFieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SingleSelectionDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.SingleSelection;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.DomainModel.FieldPackage.Number;
import com.example.taskflow.Mappers.NotificationMapper;
import com.example.taskflow.service.FieldService.FieldServiceManager;

import net.bytebuddy.utility.RandomString;

import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
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
    private FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private NotificationDAO notificationDao;
    @Autowired
    private NotificationMapper notificationMapper;

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
        AssigneeDTO fieldDto = new AssigneeDTO();

        fieldDto.setType(FieldType.ASSIGNEE);
        FieldDefinition fd = new AssigneeDefinitionBuilder()
                .setUsers(this.someUsers)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());
        
        someUsers.remove(0);
        fieldDto.setUserIds(extractIds(someUsers));

        Field createdField = fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);

        Assignee found = (Assignee) this.fieldDao.findById(createdField.getId()).orElse(null);

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
        StringDTO fieldDto = new StringDTO();

        fieldDto.setType(FieldType.TEXT);
        FieldDefinition fd = new SimpleFieldDefinitionBuilder(FieldType.TEXT)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);

        fieldDto.setFieldDefinitionId(fd.getId());

        fieldDto.setUuid(UUID.randomUUID().toString());

        fieldDto.setValue(RandomString.make(10));

        Field createdField = fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);

        Text found = (Text) this.fieldDao.findById(createdField.getId()).orElse(null);

        assertEquals(createdField, found);
    }

    @Test
    public void testCreationSingleSelection() {
        StringDTO fieldDto = new StringDTO();

        fieldDto.setType(FieldType.SINGLE_SELECTION);
        ArrayList<String> selections = new ArrayList<String>();
        selections.add("Ready");
        selections.add("In progress");
        selections.add("Done");
        FieldDefinition fd = new SingleSelectionDefinitionBuilder()                
                .setSelections(selections)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());

        fieldDto.setValue("Done");

        Field createdField = fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);
        FieldDefinition foundFD = fieldDefinitionDao.findById(createdField.getFieldDefinition().getId()).orElse(null);
        
        
        createdField.setFieldDefinition(foundFD);

        SingleSelection found = (SingleSelection) this.fieldDao.findById(createdField.getId()).orElse(null);

        assertEquals(createdField, found);
    }


    @Test
    public void testCreationNumber() {
        NumberDTO fieldDto = new NumberDTO();

        fieldDto.setType(FieldType.NUMBER);
        FieldDefinition fd = new SimpleFieldDefinitionBuilder(FieldType.NUMBER)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());

        fieldDto.setValue(BigDecimal.valueOf(Math.random()));

        Field createdField = fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);

        FieldDefinition foundFD = fieldDefinitionDao.findById(createdField.getFieldDefinition().getId()).orElse(null);
        
        
        createdField.setFieldDefinition(foundFD);

        Field found = this.fieldDao.findById(createdField.getId()).orElse(null);

        assertEquals(createdField, found);
    }

    @Test
    public void testCreationDate() {
        DateDTO dateDto = new DateDTO();

        dateDto.setType(FieldType.DATE);
        
        FieldDefinition fd = new SimpleFieldDefinitionBuilder(FieldType.DATE)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        dateDto.setFieldDefinitionId(fd.getId());
        dateDto.setUuid(UUID.randomUUID().toString());
 
        LocalDateTime date = LocalDateTime.now();
        dateDto.setDateTime(date);
        Notification notification = new Notification(UUID.randomUUID().toString(), someUsers, date.minusHours(2), "message");
        notificationDao.save(notification);
        dateDto.setNotification(this.notificationMapper.toDto(notification));

        Field createdField = fieldServiceManager.getFieldService(dateDto).pushNewField(dateDto);
        FieldDefinition foundFD = fieldDefinitionDao.findById(createdField.getFieldDefinition().getId()).orElse(null);
        
        
        createdField.setFieldDefinition(foundFD);

        Field found = this.fieldDao.findById(createdField.getId()).orElse(null);

        assertEquals(createdField, found);
    }

    @Test
    public void testUpdateAssignee(){

        AssigneeDTO fieldDto = new AssigneeDTO();

        fieldDto.setType(FieldType.ASSIGNEE);
        FieldDefinition fd = new AssigneeDefinitionBuilder()
                .setUsers(this.someUsers)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());
        
        someUsers.remove(0);
        fieldDto.setUserIds(extractIds(someUsers));

        Field createdField = fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);

        // update

        AssigneeDTO newFieldDto = new AssigneeDTO();

        newFieldDto.setType(FieldType.ASSIGNEE);

        newFieldDto.setId(createdField.getId());
        newFieldDto.setFieldDefinitionId(fd.getId());
        newFieldDto.setUuid(UUID.randomUUID().toString());
        
        ArrayList<User> newUsers = new ArrayList<User>();
        newUsers.add(someUsers.remove(0));
        newFieldDto.setUserIds(extractIds(newUsers));

        fieldServiceManager.getFieldService(fieldDto).updateField(newFieldDto);

        Assignee fieldFromDb = (Assignee) this.fieldDao.findById(newFieldDto.getId()).orElseThrow();

        assertTrue(fieldFromDb.getUsers().size() == 1);
    }

    @Test
    public void testUpdateText(){

        StringDTO fieldDto = new StringDTO();

        fieldDto.setType(FieldType.TEXT);
        FieldDefinition fd = new SimpleFieldDefinitionBuilder(FieldType.TEXT)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);

        fieldDto.setFieldDefinitionId(fd.getId());


        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());

        fieldDto.setValue(RandomString.make(10));

        Field createdField = fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);

        // update

        StringDTO newFieldDto = new StringDTO();

        newFieldDto.setType(FieldType.TEXT);

        newFieldDto.setFieldDefinitionId(fd.getId());
        newFieldDto.setId(createdField.getId());
        newFieldDto.setUuid(UUID.randomUUID().toString());
        newFieldDto.setValue("newValue");

        this.fieldServiceManager.getFieldService(newFieldDto).updateField(newFieldDto);

        Text fieldFromBd = (Text) this.fieldDao.findById(newFieldDto.getId()).orElseThrow();

        assertEquals(fieldFromBd.getValue(), "newValue");
    }

    @Test
    public void testUpdateSingleSelection() {
        StringDTO fieldDto = new StringDTO();

        fieldDto.setType(FieldType.SINGLE_SELECTION);
        ArrayList<String> selections = new ArrayList<String>();
        selections.add("Ready");
        selections.add("In progress");
        selections.add("Done");
        FieldDefinition fd = new SingleSelectionDefinitionBuilder()                
                .setSelections(selections)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());

        fieldDto.setValue("Done");

        Field createdField = fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);        

        // update

        StringDTO newFieldDto = new StringDTO();

        newFieldDto.setId(createdField.getId());
        newFieldDto.setType(FieldType.SINGLE_SELECTION);
        newFieldDto.setFieldDefinitionId(fd.getId());
        newFieldDto.setValue("Ready");

        this.fieldServiceManager.getFieldService(newFieldDto).updateField(newFieldDto);

        SingleSelection fieldFromDb = (SingleSelection) this.fieldDao.findById(newFieldDto.getId()).orElseThrow();
        
        assertEquals(fieldFromDb.getValue(), "Ready");
    }


    @Test
    public void testUpdateNumber() {
        NumberDTO fieldDto = new NumberDTO();

        fieldDto.setType(FieldType.NUMBER);
        FieldDefinition fd = new SimpleFieldDefinitionBuilder(FieldType.NUMBER)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        fieldDto.setFieldDefinitionId(fd.getId());
        fieldDto.setUuid(UUID.randomUUID().toString());

        fieldDto.setValue(BigDecimal.valueOf(Math.random()));

        Field createdField = fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);        

        // update

        NumberDTO newFieldDto = new NumberDTO();

        newFieldDto.setType(FieldType.NUMBER);

        newFieldDto.setId(createdField.getId());
        newFieldDto.setFieldDefinitionId(fd.getId());
        newFieldDto.setUuid(UUID.randomUUID().toString());

        newFieldDto.setValue(BigDecimal.valueOf(9.9));

        this.fieldServiceManager.getFieldService(newFieldDto).updateField(newFieldDto);

        Number fieldFromDb = (Number) this.fieldDao.findById(newFieldDto.getId()).orElseThrow();

        assertEquals(fieldFromDb.getValue(), BigDecimal.valueOf(9.9));
    }

    @Test
    public void testUpdateDate() {
        DateDTO dateDto = new DateDTO();

        dateDto.setType(FieldType.DATE);
        
        FieldDefinition fd = new SimpleFieldDefinitionBuilder(FieldType.DATE)
                .setName("meet")
                .build();

        this.fieldDefinitionDao.save(fd);
        dateDto.setFieldDefinitionId(fd.getId());
        dateDto.setUuid(UUID.randomUUID().toString());
 
        LocalDateTime date = LocalDateTime.now();
        dateDto.setDateTime(date);
        Notification notification = new Notification(UUID.randomUUID().toString(), someUsers, date.minusHours(2), "message");
        notificationDao.save(notification);
        dateDto.setNotification(this.notificationMapper.toDto(notification));

        Field createdField = fieldServiceManager.getFieldService(dateDto).pushNewField(dateDto);

        // update

        DateDTO newDateDto = new DateDTO();

        newDateDto.setType(FieldType.DATE);

        newDateDto.setFieldDefinitionId(fd.getId());
        newDateDto.setUuid(UUID.randomUUID().toString());
        newDateDto.setId(createdField.getId());
 
        LocalDateTime newDate = LocalDateTime.of(2001, 9, 11, 0, 0, 0);
        newDateDto.setDateTime(newDate);
        Notification newNotification = new Notification(UUID.randomUUID().toString(), someUsers, date.plusHours(5), "newMessage");
        notificationDao.save(newNotification);
        newDateDto.setNotification(this.notificationMapper.toDto(newNotification));

        this.fieldServiceManager.getFieldService(newDateDto).updateField(newDateDto);
    }

}

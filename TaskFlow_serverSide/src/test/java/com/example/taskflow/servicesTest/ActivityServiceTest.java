package com.example.taskflow.servicesTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DTOs.Field.StringDTO;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.FieldMapper;
import com.example.taskflow.Mappers.NotificationMapper;
import com.example.taskflow.service.ActivityService;
import com.example.taskflow.service.FieldService.FieldServiceManager;

import net.bytebuddy.utility.RandomString;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class ActivityServiceTest {

    @Autowired
    private TestUtil testUtil;
    @Autowired
    private FieldServiceManager fieldServiceManager;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private NotificationDAO notificationDao;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private ActivityService activityService;

    private ArrayList<User> someUsers = new ArrayList<User>();

    @BeforeEach
    public void setupDatabase() {
        this.testUtil.cleanDatabase();
        for (int i = 0; i < 5; i++) {
            this.testUtil.addGetRandomUserToDatabase();
        }
        this.someUsers = this.userDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));

    }

    @Test
    public void testCreationActivity() {
        ActivityDTO activityDTO = new ActivityDTO();

        activityDTO.setName("name");
        ArrayList<FieldDTO> fieldsDto = new ArrayList<FieldDTO>();

        // ---------------------- ASSIGNEE ---------------------- //

        AssigneeDTO assigneeDto = new AssigneeDTO();

        assigneeDto.setType(FieldType.ASSIGNEE);
        FieldDefinition fd = FieldDefinitionFactory.getBuilder(FieldType.ASSIGNEE)
                .setName("assignee")
                .addParameters(this.someUsers)
                .build();

        this.fieldDefinitionDao.save(fd);
        assigneeDto.setFieldDefinitionId(fd.getId());
        assigneeDto.setUuid(UUID.randomUUID().toString());

        someUsers.remove(0);
        assigneeDto.setUserIds(this.extractIds(someUsers));

        FieldDTO createdFieldDto = fieldServiceManager.getFieldService(assigneeDto).createField(assigneeDto);
        Field assignee = this.fieldMapper.toEntity(createdFieldDto);
        fieldsDto.add(createdFieldDto);

        // ---------------------- TEXT ---------------------- //

        StringDTO textDto = new StringDTO();

        textDto.setType(FieldType.TEXT);
        fd = FieldDefinitionFactory.getBuilder(FieldType.TEXT)
                .setName("text")
                .build();

        this.fieldDefinitionDao.save(fd);
        textDto.setFieldDefinitionId(fd.getId());
        textDto.setUuid(UUID.randomUUID().toString());

        textDto.setValue(RandomString.make(10));

        createdFieldDto = fieldServiceManager.getFieldService(textDto).createField(textDto);
        Field text = this.fieldMapper.toEntity(createdFieldDto);
        fieldsDto.add(createdFieldDto);

        //fields.add(text);

        // ---------------------- SINGLE SELECTION ---------------------- //

        StringDTO singleSelectionDto = new StringDTO();

        singleSelectionDto.setType(FieldType.SINGLE_SELECTION);
        ArrayList<String> selections = new ArrayList<String>();
        selections.add("Ready");
        selections.add("In progress");
        selections.add("Done");
        fd = FieldDefinitionFactory.getBuilder(FieldType.SINGLE_SELECTION)
                .setName("single selection")
                .addParameters(selections)
                .build();

        this.fieldDefinitionDao.save(fd);
        singleSelectionDto.setFieldDefinitionId(fd.getId());
        singleSelectionDto.setUuid(UUID.randomUUID().toString());

        singleSelectionDto.setValue(RandomString.make(10));

        createdFieldDto = fieldServiceManager.getFieldService(singleSelectionDto).createField(singleSelectionDto);
        Field selection = this.fieldMapper.toEntity(createdFieldDto);
        fieldsDto.add(createdFieldDto);

        // ---------------------- NUMBER ---------------------- //

        NumberDTO numberDto = new NumberDTO();

        numberDto.setType(FieldType.NUMBER);
        fd = FieldDefinitionFactory.getBuilder(FieldType.NUMBER)
                .setName("number")
                .build();

        this.fieldDefinitionDao.save(fd);
        numberDto.setFieldDefinitionId(fd.getId());
        numberDto.setUuid(UUID.randomUUID().toString());

        numberDto.setValue(new Random().nextFloat());

        createdFieldDto = fieldServiceManager.getFieldService(numberDto).createField(numberDto);
        Field number = this.fieldMapper.toEntity(createdFieldDto);
        fieldsDto.add(createdFieldDto);

        // ---------------------- DATE ---------------------- //

        DateDTO dateDto = new DateDTO();

        dateDto.setType(FieldType.DATE);
        fd = FieldDefinitionFactory.getBuilder(FieldType.DATE)
                .setName("date")
                .build();

        this.fieldDefinitionDao.save(fd);
        dateDto.setFieldDefinitionId(fd.getId());
        dateDto.setUuid(UUID.randomUUID().toString());

        //creazione DateData con notifica
        LocalDateTime dateTime = LocalDateTime.now();
        dateDto.setDateTime(dateTime);
        
        Notification notification = new Notification(someUsers, dateTime.minusHours(2), "message");
        
        notification = notificationDao.save(notification);
        dateDto.setNotification(this.notificationMapper.toDto(notification));

        createdFieldDto = fieldServiceManager.getFieldService(dateDto).createField(dateDto);
        Field date = this.fieldMapper.toEntity(createdFieldDto);
        fieldsDto.add(createdFieldDto);

        //fields.add(date);

        // ---------------------- DOCUMENT ---------------------- //
        //TODO da implementare


        activityDTO.setFields(fieldsDto);
        activityService.createActivity(activityDTO);
    }

    private ArrayList<String> extractIds(ArrayList<User> users) {

        ArrayList<String> ids = new ArrayList<String>();
        for (User user : users) {
            ids.add(user.getId());
        }

        return ids;
    }
}

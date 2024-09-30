package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.swing.text.html.parser.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DTOs.Field.SingleSelectionDTO;
import com.example.taskflow.DTOs.Field.TextDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.EntityFactory;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.AssigneeDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SimpleFieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SingleSelectionDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.NotificationMapper;
import com.example.taskflow.service.ActivityService;

import net.bytebuddy.utility.RandomString;

@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class ActivityServiceTest {

    @Autowired
    private TestUtil testUtil;
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
    @Autowired
    private ActivityDAO activityDao;
    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private ProjectDAO projectDao;

    private ArrayList<User> someUsers;

    @BeforeEach
    public void setupDatabase() {
        //this.testUtil.cleanDatabase();
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
        FieldDefinition fd = new AssigneeDefinitionBuilder()
                .setUsers(this.someUsers)
                .setName("assignee")
                .build();

        this.fieldDefinitionDao.save(fd);
        assigneeDto.setFieldDefinitionId(fd.getId());
        assigneeDto.setUuid(UUID.randomUUID().toString());

        someUsers.remove(0);
        assigneeDto.setUserIds(this.extractIds(someUsers));

        //FieldDTO createdFieldDto = fieldServiceManager.getFieldService(assigneeDto).pushNewField(assigneeDto);
        
        fieldsDto.add(assigneeDto);

        // ---------------------- TEXT ---------------------- //

        TextDTO textDto = new TextDTO();

        textDto.setType(FieldType.TEXT);
        fd = new SimpleFieldDefinitionBuilder(FieldType.TEXT)
                .setName("text")
                .build();

        this.fieldDefinitionDao.save(fd);
        textDto.setFieldDefinitionId(fd.getId());
        textDto.setUuid(UUID.randomUUID().toString());

        textDto.setValue(RandomString.make(10));

        //createdFieldDto = fieldServiceManager.getFieldService(textDto).pushNewField(textDto);
        fieldsDto.add(textDto);

        //fields.add(text);

        // ---------------------- SINGLE SELECTION ---------------------- //

        SingleSelectionDTO singleSelectionDto = new SingleSelectionDTO();

        singleSelectionDto.setType(FieldType.SINGLE_SELECTION);
        ArrayList<String> selections = new ArrayList<String>();
        selections.add("Ready");
        selections.add("In progress");
        selections.add("Done");
        fd = new SingleSelectionDefinitionBuilder()
                .setSelections(selections)
                .setName("single selection")
                .build();

        this.fieldDefinitionDao.save(fd);
        singleSelectionDto.setFieldDefinitionId(fd.getId());
        singleSelectionDto.setUuid(UUID.randomUUID().toString());

        singleSelectionDto.setValue("Done");

        //createdFieldDto = fieldServiceManager.getFieldService(singleSelectionDto).pushNewField(singleSelectionDto);
        fieldsDto.add(singleSelectionDto);

        // ---------------------- NUMBER ---------------------- //

        NumberDTO numberDto = new NumberDTO();

        numberDto.setType(FieldType.NUMBER);
        fd = new SimpleFieldDefinitionBuilder(FieldType.NUMBER)
                .setName("number")
                .build();

        this.fieldDefinitionDao.save(fd);
        numberDto.setFieldDefinitionId(fd.getId());
        numberDto.setUuid(UUID.randomUUID().toString());

        numberDto.setValue(BigDecimal.valueOf(Math.random()));

        //createdFieldDto = fieldServiceManager.getFieldService(numberDto).pushNewField(numberDto);
        fieldsDto.add(numberDto);

        // ---------------------- DATE ---------------------- //

        DateDTO dateDto = new DateDTO();

        dateDto.setType(FieldType.DATE);
        fd = new SimpleFieldDefinitionBuilder(FieldType.DATE)
                .setName("date")
                .build();

        this.fieldDefinitionDao.save(fd);
        dateDto.setFieldDefinitionId(fd.getId());
        dateDto.setUuid(UUID.randomUUID().toString());

        //creazione DateData con notifica
        LocalDateTime dateTime = LocalDateTime.now();
        dateDto.setDateTime(dateTime);
        
        Notification notification = new Notification(UUID.randomUUID().toString(), someUsers, dateTime.minusHours(2), "message");
        
        notification = notificationDao.save(notification);
        dateDto.setNotification(this.notificationMapper.toDto(notification));

        //createdFieldDto = fieldServiceManager.getFieldService(dateDto).pushNewField(dateDto);
        fieldsDto.add(dateDto);

        // ---------------------- DOCUMENT ---------------------- //
        //TODO da implementare


        activityDTO.setFields(fieldsDto);
        activityService.pushNewActivity(activityDTO);
    }

    @Test
    public void testDelete(){
        Organization organization = this.testUtil.getEntireDatabaseMockup(1, 10, 5, 30).get(0);
        
        ArrayList<Project> projects = organization.getProjects();

        for (Project project : projects){
            ArrayList<Activity> activities = project.getActivities();

            for (Activity activity : activities){
                ArrayList<Field> fields = activity.getFields();
                
                this.activityService.deleteActivityAndFields(activity.getId());
                assertFalse(this.activityDao.findById(activity.getId()).isPresent());
                for (Field field : fields){
                    assertFalse(this.fieldDao.findById(field.getId()).isPresent());
                }
                
                Project projectFromDb = this.projectDao.findById(project.getId()).orElseThrow();
                assertFalse(projectFromDb.getActivities().contains(activity));
            }
        }
    }

    private ArrayList<String> extractIds(ArrayList<User> users) {

        ArrayList<String> ids = new ArrayList<String>();
        for (User user : users) {
            ids.add(user.getId());
        }

        return ids;
    }
}

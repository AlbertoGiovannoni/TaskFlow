package com.unifi.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.unifi.taskflow.TestUtil;
import com.unifi.taskflow.businessLogic.dtos.ActivityDTO;
import com.unifi.taskflow.businessLogic.dtos.field.AssigneeDTO;
import com.unifi.taskflow.businessLogic.dtos.field.DateDTO;
import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.dtos.field.NumberDTO;
import com.unifi.taskflow.businessLogic.dtos.field.SingleSelectionDTO;
import com.unifi.taskflow.businessLogic.dtos.field.TextDTO;
import com.unifi.taskflow.businessLogic.mappers.NotificationMapper;
import com.unifi.taskflow.businessLogic.services.ActivityService;
import com.unifi.taskflow.daos.ActivityDAO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.NotificationDAO;
import com.unifi.taskflow.daos.ProjectDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.Activity;
import com.unifi.taskflow.domainModel.Notification;
import com.unifi.taskflow.domainModel.Organization;
import com.unifi.taskflow.domainModel.Project;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;
import com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders.AssigneeDefinitionBuilder;
import com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders.SimpleFieldDefinitionBuilder;
import com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders.SingleSelectionDefinitionBuilder;
import com.unifi.taskflow.domainModel.fields.Field;

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

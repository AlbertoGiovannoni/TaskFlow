package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DTOs.Field.SingleSelectionDTO;
import com.example.taskflow.DTOs.Field.TextDTO;
import com.example.taskflow.DTOs.FieldDefinition.AssigneeDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SingleSelectionDefinitionDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.service.ActivityService;
import com.example.taskflow.service.ProjectService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionServiceManager;

import net.bytebuddy.utility.RandomString;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class FieldDefinitionServiceTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private FieldDefinitionServiceManager fieldDefinitionServiceManager;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityDAO activityDao;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectDAO projectDao;
    @Autowired
    private FieldDAO fieldDao;
    @Autowired 
    private FieldDefinitionDAO fieldDefinitionDao;

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
    }

    @Test
    public void testCreation(){
        FieldDefinitionDTO fieldDefinitionDto;
        FieldDefinition createdFieldDefinition;

        for (FieldType type : FieldType.values()){
            fieldDefinitionDto = this.getFieldDefinitionDTO(type);
            fieldDefinitionDto.setName(RandomString.make(10));

            if (type == FieldType.ASSIGNEE){
                ArrayList<User> users = this.testUtil.addGetMultipleRandomUserToDatabase(10);
                ((AssigneeDefinitionDTO)fieldDefinitionDto).setAssigneeIds(this.getUserIds(users));
            }

            if (type == FieldType.SINGLE_SELECTION){
                ((SingleSelectionDefinitionDTO)fieldDefinitionDto).setSelections(this.getRandomSelections(10));
            }

            createdFieldDefinition = this.fieldDefinitionServiceManager
                                                        .getFieldDefinitionService(fieldDefinitionDto)
                                                        .pushNewFieldDefinition(fieldDefinitionDto);
        
            assertEquals(createdFieldDefinition, this.fieldDefinitionDao.findById(createdFieldDefinition.getId()).orElse(null));
        }
    }

    private ArrayList<String> getUserIds(ArrayList<User> users){
        ArrayList<String> userIds = new ArrayList<>();

        for (User user : users){
            userIds.add(user.getId());
        }

        return userIds;
    }

    private ArrayList<String> getRandomSelections(int n){
        ArrayList<String> selections = new ArrayList<>();

        for (int i = 0; i < n; i++){
            selections.add(RandomString.make(10));
        }

        return selections;
    }

    @Test
    public void testCascadingDelete(){
        FieldDefinitionDTO fieldDefinitionDto;
        FieldDefinition createdFieldDefinition;

        ArrayList<ActivityDTO> activityDtos;
        ArrayList<FieldDTO> fieldDTOs;

        ArrayList<Activity> activitiesPushed;

        ProjectDTO projectDto;
        Project movingProject;

        for (FieldType type : FieldType.values()){
            if (type != FieldType.DOCUMENT){
                projectDto = new ProjectDTO();
                projectDto.setName(RandomString.make(10));
                projectDto = this.projectService.pushNewProject(projectDto);

                fieldDefinitionDto = this.getFieldDefinitionDTO(type);
                fieldDefinitionDto.setName(RandomString.make(10));

                if (type == FieldType.ASSIGNEE){
                    ArrayList<User> users = this.testUtil.addGetMultipleRandomUserToDatabase(10);
                    ((AssigneeDefinitionDTO)fieldDefinitionDto).setAssigneeIds(this.getUserIds(users));
                }
    
                if (type == FieldType.SINGLE_SELECTION){
                    ((SingleSelectionDefinitionDTO)fieldDefinitionDto).setSelections(this.getRandomSelections(10));
                }

                createdFieldDefinition = this.fieldDefinitionServiceManager
                                                            .getFieldDefinitionService(fieldDefinitionDto)
                                                            .pushNewFieldDefinition(fieldDefinitionDto);
                
                fieldDTOs = this.getFieldDTOArray(10, type);
                this.setupFieldDTOs(fieldDTOs, createdFieldDefinition);

                activityDtos = this.getActivitieDTOsArray(fieldDTOs);
                activitiesPushed = this.pushAllActivities(activityDtos);

                movingProject = this.projectDao.findById(projectDto.getId()).orElseThrow();
                movingProject.addFieldDefinition(createdFieldDefinition);
                
                for (Activity activity : activitiesPushed){
                    movingProject.addActivity(activity);
                }
                this.projectDao.save(movingProject);
    
                this.fieldDefinitionServiceManager
                    .getFieldDefinitionService(fieldDefinitionDto)
                    .deleteFieldDefinitionAndFieldsAndReferenceToFieldsInActivity(createdFieldDefinition.getId());

                for (Activity activity : activitiesPushed){
                    ArrayList<Field> activityFields = activity.getFields();
                    for (Field field : activityFields){
                        assertNull(this.fieldDao.findById(field.getId()).orElse(null));
                    }

                    Activity activityFromDb = this.activityDao.findById(activity.getId()).orElseThrow();
                    ArrayList<Field> activityFieldsFromDb = activityFromDb.getFields();
                    assertEquals(0, activityFieldsFromDb.size());
                }

                Project projectFromDb = this.projectDao.findById(projectDto.getId()).orElseThrow();
                assertEquals(0, projectFromDb.getFieldsTemplate().size());
            }
        }
    }

    private FieldDefinitionDTO getFieldDefinitionDTO(FieldType type){
        FieldDefinitionDTO fieldDefinitionDTO;
        switch (type) {
            case ASSIGNEE:
                fieldDefinitionDTO =  new AssigneeDefinitionDTO();
                break;
            case SINGLE_SELECTION:
                fieldDefinitionDTO =  new SingleSelectionDefinitionDTO();
                break;
            default:
                fieldDefinitionDTO =  new SimpleFieldDefinitionDTO();
                break;
        }
        fieldDefinitionDTO.setType(type);
        return fieldDefinitionDTO;
    }

    private ArrayList<FieldDTO> getFieldDTOArray(int n, FieldType type){
        ArrayList<FieldDTO> fieldDTOs = new ArrayList<>();

        for (int i = 0; i < n; i++){
            fieldDTOs.add(this.getFieldDTO(type));
        }

        return fieldDTOs;
    }

    private void setupFieldDTOs(ArrayList<FieldDTO> fieldDtos, FieldDefinition fieldDefinition){
        for (FieldDTO fieldDto : fieldDtos){
            fieldDto.setFieldDefinitionId(fieldDefinition.getId());
            this.setValueToDTO(fieldDto, fieldDefinition);
        }
    }

    private void setValueToDTO(FieldDTO fieldDto, FieldDefinition fieldDefinition){
        switch (fieldDto.getType()) {
            case ASSIGNEE:
                ((AssigneeDTO)fieldDto)
                    .setUserIds(
                        this.getUserIds(((AssigneeDefinition)fieldDefinition).getPossibleAssigneeUsers()));
                break;
            case SINGLE_SELECTION:
                ((SingleSelectionDTO)fieldDto).setValue((((SingleSelectionDefinition)fieldDefinition).getPossibleSelections().get(0)));
                break;
            case TEXT:
                ((TextDTO)fieldDto).setValue(RandomString.make(100));
                break;
            case DATE:
                ((DateDTO)fieldDto).setDateTime(LocalDateTime.now());
                break;
            case NUMBER:
                ((NumberDTO)fieldDto).setValue(new BigDecimal(100));
                break;
            default:
                throw new IllegalArgumentException("Document need implementation");
        }
    }

    private FieldDTO getFieldDTO(FieldType type){
        FieldDTO fieldDTO;
        switch (type) {
            case ASSIGNEE:
                fieldDTO =  new AssigneeDTO();
                break;
            case SINGLE_SELECTION:
                fieldDTO = new SingleSelectionDTO();
                break;
            case TEXT:
                fieldDTO =  new TextDTO();
                break;
            case DATE:
                fieldDTO = new DateDTO();
                break;
            case NUMBER:
                fieldDTO = new NumberDTO();
                break;
            default:
                throw new IllegalArgumentException("Document need implementation");
        }
        fieldDTO.setType(type);
        return fieldDTO;
    }

    private ArrayList<ActivityDTO> getActivitieDTOsArray(ArrayList<FieldDTO> fieldDtos){
        if (fieldDtos.size() == 0){
            throw new IllegalArgumentException("Size must be > 0");
        }

        ArrayList<ActivityDTO> activityDtos = new ArrayList<>();
        ActivityDTO movingActivityDto;

        ArrayList<FieldDTO> fieldDTOsTempArray = new ArrayList<>();

        for (FieldDTO fieldDto : fieldDtos){
            fieldDTOsTempArray.clear();
            fieldDTOsTempArray.add(fieldDto);

            movingActivityDto = new ActivityDTO();
            movingActivityDto.setName(RandomString.make(10));
            movingActivityDto.setFields(fieldDTOsTempArray);

            activityDtos.add(movingActivityDto);
        }

        return activityDtos;
    }

    private ArrayList<Activity> pushAllActivities(ArrayList<ActivityDTO> activities){
        ArrayList<Activity> pushedActivities = new ArrayList<>();

        for (ActivityDTO activity : activities){
            pushedActivities.add(this.activityService.pushNewActivity(activity));
        }

        return pushedActivities;
    }
}

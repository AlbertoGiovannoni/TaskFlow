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

import com.example.taskflow.DAOs.*;
import com.example.taskflow.DTOs.Field.*;
import com.example.taskflow.DTOs.FieldDefinition.*;
import com.example.taskflow.DTOs.*;
import com.example.taskflow.DomainModel.*;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.*;
import com.example.taskflow.DomainModel.FieldPackage.*;
import com.example.taskflow.service.*;

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
            fieldDefinitionDto = this.testUtil.getFieldDefinitionDTO(type);
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

                fieldDefinitionDto = this.testUtil.getFieldDefinitionDTO(type);
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
                
                fieldDTOs = this.testUtil.getFieldDTOArray(10, type);
                this.testUtil.setupFieldDTOs(fieldDTOs, createdFieldDefinition);

                activityDtos = this.testUtil.getActivitieDTOsArray(fieldDTOs);
                activitiesPushed = this.testUtil.pushAllActivities(activityDtos);

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
}

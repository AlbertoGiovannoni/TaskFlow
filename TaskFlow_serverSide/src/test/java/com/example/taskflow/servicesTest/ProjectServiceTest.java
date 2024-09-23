package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.ProjectMapper;
import com.example.taskflow.service.ProjectService;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class ProjectServiceTest {

    @Autowired
    private TestUtil testUtil;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDao;

    @BeforeEach
    public void setupDatabase() {
        this.testUtil.cleanDatabase();
        for (int i = 0; i < 5; i++) {
            this.testUtil.addGetRandomUserToDatabase();
        }

    }

    @Test
    public void testCreationProject() {
        // creazione projectDto
        ArrayList<FieldDefinition> fieldDefinitions = new ArrayList<FieldDefinition>();
        FieldDefinition fieldDefinition = new SimpleFieldDefinition(UUID.randomUUID().toString(), "prova", FieldType.TEXT);
        fieldDefinitions.add(fieldDefinition);

        Project project = new Project(UUID.randomUUID().toString(), "projectName", fieldDefinitions, new ArrayList<Activity>());

        ProjectDTO projectDTO = projectMapper.toDto(project);

        projectDTO = projectService.createProject(projectDTO);

        Project projectFromDb = projectDAO.findById(projectDTO.getId()).orElse(null);

        assertEquals(projectDTO.getId(), projectFromDb.getId());

    }

    @Test
    public void testAddActivity(){
        ArrayList<Field> fields = new ArrayList<Field>();

        
        Activity activity = new Activity(UUID.randomUUID().toString(), "activity", fields);
        
    }

    @Test
    public void testAddFieldDefinition(){
        Project project = new Project(UUID.randomUUID().toString(), "projectName", new ArrayList<FieldDefinition>(), new ArrayList<Activity>());
        project = projectDAO.save(project);

        FieldDefinitionDTO simpleFieldDefinitionDTO = new SimpleFieldDefinitionDTO();
        simpleFieldDefinitionDTO.setName("prova");
        simpleFieldDefinitionDTO.setType(FieldType.TEXT);
        simpleFieldDefinitionDTO.setUuid(UUID.randomUUID().toString());

        projectService.addFieldDefinitionToProject(project.getId(), simpleFieldDefinitionDTO);

        Project projectFrotDb = projectDAO.findById(project.getId()).orElse(null);

        FieldDefinition fieldDefinitionFromDb = fieldDefinitionDao.findById(projectFrotDb.getFieldsTemplate().get(0).getId()).orElse(null);
        assertEquals(fieldDefinitionFromDb.getName(), simpleFieldDefinitionDTO.getName());
    }
    
}

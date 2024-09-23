package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
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
        Project project = new Project("projectName", new ArrayList<FieldDefinition>(), new ArrayList<Activity>());

        ProjectDTO projectDTO = projectMapper.toDto(project);

        projectDTO = projectService.createProject(projectDTO);

        Project projectFromDb = projectDAO.findById(projectDTO.getId()).orElse(null);

        assertEquals(projectDTO.getId(), projectFromDb.getId());

    }

    @Test
    public void testAddActivity(){
        ArrayList<Field> fields = new ArrayList<Field>();

        
        Activity activity = new Activity("activity", fields);
        
    }
    
}

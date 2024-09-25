package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.Field.StringDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.Mappers.ActivityMapper;
import com.example.taskflow.Mappers.ProjectMapper;
import com.example.taskflow.service.ProjectService;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionService;

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
    @Autowired
    private FieldDefinitionService fieldDefinitionService;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityDAO activityDAO;

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
        FieldDefinition fieldDefinition = new SimpleFieldDefinition(UUID.randomUUID().toString(), "prova",
                FieldType.TEXT);
        fieldDefinitions.add(fieldDefinition);

        Project project = new Project(UUID.randomUUID().toString(), "projectName", fieldDefinitions,
                new ArrayList<Activity>());

        ProjectDTO projectDTO = projectMapper.toDto(project);

        projectDTO = projectService.pushNewProject(projectDTO);

        Project projectFromDb = projectDAO.findById(projectDTO.getId()).orElse(null);

        assertEquals(projectDTO.getId(), projectFromDb.getId());

    }

    @Test
    public void testAddActivity() {
        ArrayList<Field> fields = new ArrayList<Field>();

        FieldDefinitionDTO simpleFieldDefinitionDTO = new SimpleFieldDefinitionDTO();
        simpleFieldDefinitionDTO.setName("prova");
        simpleFieldDefinitionDTO.setType(FieldType.TEXT);
        FieldDefinition textDefinition = this.fieldDefinitionService.pushNewFieldDefinition(simpleFieldDefinitionDTO);

        Field field = new Text(UUID.randomUUID().toString(), textDefinition, "test");
        fields.add(field);

        Activity activity = new Activity(UUID.randomUUID().toString(), "activity", fields);
        ActivityDTO activityDTO = this.activityMapper.toDto(activity);

        Project project = new Project(UUID.randomUUID().toString(), "projectName", new ArrayList<FieldDefinition>(),
                new ArrayList<Activity>());
        project = projectDAO.save(project);

        this.projectService.addActivityToProject(project.getId(), activityDTO);

        project = projectDAO.findById(project.getId()).orElseThrow();

        String valuePushed = ((StringDTO) activityDTO.getFields().get(0)).getValue();
        String valueFromDb = ((Text) project.getActivities().get(0).getFields().get(0)).getValue();

        assertEquals(valuePushed, valueFromDb);

    }

    @Test
    public void testAddFieldDefinition() {
        Project project = new Project(UUID.randomUUID().toString(), "projectName", new ArrayList<FieldDefinition>(),
                new ArrayList<Activity>());
        project = projectDAO.save(project);

        FieldDefinitionDTO simpleFieldDefinitionDTO = new SimpleFieldDefinitionDTO();
        simpleFieldDefinitionDTO.setName("prova");
        simpleFieldDefinitionDTO.setType(FieldType.TEXT);

        projectService.addFieldDefinitionToProject(project.getId(), simpleFieldDefinitionDTO);

        Project projectFrotDb = projectDAO.findById(project.getId()).orElse(null);

        FieldDefinition fieldDefinitionFromDb = fieldDefinitionDao
                .findById(projectFrotDb.getFieldsTemplate().get(0).getId()).orElse(null);
        assertEquals(fieldDefinitionFromDb.getName(), simpleFieldDefinitionDTO.getName());
    }

    @Test
    public void testRenameProject() {
        Project project = new Project(UUID.randomUUID().toString(), "projectName", new ArrayList<FieldDefinition>(),
                new ArrayList<Activity>());
        project = projectDAO.save(project);

        String newName = "newName";

        this.projectService.renameProject(project.getId(), newName);

        Project projectFromDb = projectDAO.findById(project.getId()).orElseThrow();

        assertEquals(newName, projectFromDb.getName());
    }

    @Test
    public void testDeleteProject() {
        
        Project project = new Project(UUID.randomUUID().toString(), "projectName", new ArrayList<FieldDefinition>(),
        new ArrayList<Activity>());
        project = projectDAO.save(project);

        FieldDefinitionDTO simpleFieldDefinitionDTO = new SimpleFieldDefinitionDTO();
        simpleFieldDefinitionDTO.setName("prova");
        simpleFieldDefinitionDTO.setType(FieldType.TEXT);
        FieldDefinition textDefinition = this.fieldDefinitionService.pushNewFieldDefinition(simpleFieldDefinitionDTO);

        ArrayList<Field> fields = new ArrayList<Field>();
        Field field = new Text(UUID.randomUUID().toString(), textDefinition, "test");
        fields.add(field);

        
        Activity activity = new Activity(UUID.randomUUID().toString(), "activity", fields);
        ActivityDTO activityDTO = this.activityMapper.toDto(activity);

        ProjectDTO projectDto = this.projectService.addActivityToProject(project.getId(), activityDTO);
        ArrayList<ActivityDTO> actDto = projectDto.getActivities();

        this.projectService.deleteProject(project.getId());

        assertNull(activityDAO.findById(actDto.get(0).getId()).orElse(null));
        assertNull(projectDAO.findById(project.getId()).orElse(null));
    }

}

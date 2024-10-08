package com.unifi.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.unifi.taskflow.TestUtil;
import com.unifi.taskflow.businessLogic.dtos.ActivityDTO;
import com.unifi.taskflow.businessLogic.dtos.ProjectDTO;
import com.unifi.taskflow.businessLogic.dtos.field.TextDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.FieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.SimpleFieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.mappers.ActivityMapper;
import com.unifi.taskflow.businessLogic.mappers.ProjectMapper;
import com.unifi.taskflow.businessLogic.services.ProjectService;
import com.unifi.taskflow.businessLogic.services.fieldDefinitionServices.FieldDefinitionServiceManager;
import com.unifi.taskflow.daos.ActivityDAO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.OrganizationDAO;
import com.unifi.taskflow.daos.ProjectDAO;
import com.unifi.taskflow.domainModel.Activity;
import com.unifi.taskflow.domainModel.BaseEntity;
import com.unifi.taskflow.domainModel.Organization;
import com.unifi.taskflow.domainModel.Project;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;
import com.unifi.taskflow.domainModel.fieldDefinitions.SimpleFieldDefinition;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.Text;

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
    private FieldDefinitionServiceManager fieldDefinitionServiceManager;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private OrganizationDAO organizationDAO;

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
        FieldDefinition textDefinition = this.fieldDefinitionServiceManager
                                    .getFieldDefinitionService(simpleFieldDefinitionDTO)
                                    .pushNewFieldDefinition(simpleFieldDefinitionDTO);

        Field field = new Text(UUID.randomUUID().toString(), textDefinition, "test");
        fields.add(field);

        Activity activity = new Activity(UUID.randomUUID().toString(), "activity", fields);
        ActivityDTO activityDTO = this.activityMapper.toDto(activity);

        Project project = new Project(UUID.randomUUID().toString(), "projectName", new ArrayList<FieldDefinition>(),
                new ArrayList<Activity>());
        project = projectDAO.save(project);

        this.projectService.addActivityToProject(project.getId(), activityDTO);

        project = projectDAO.findById(project.getId()).orElseThrow();

        String valuePushed = ((TextDTO) activityDTO.getFields().get(0)).getValue();
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
        Organization organization = this.testUtil.getEntireDatabaseMockup(1, 10, 3, 20).get(0);

        ArrayList<Project> projects = organization.getProjects();

        for (Project project : projects){
            ArrayList<FieldDefinition> fieldDefinitions = project.getFieldsTemplate();
            
            ArrayList<Activity> activities = project.getActivities();
            ArrayList<Field> fields = new ArrayList<>();
            
            for (Activity activity : activities){
                fields.addAll(activity.getFields());
            }

            this.projectService.deleteProject(project.getId());

            assertTrue(this.activityDAO.findAllById(this.getIds(activities)).isEmpty());
            assertTrue(this.fieldDao.findAllById(this.getIds(fields)).isEmpty());
            assertTrue(this.fieldDefinitionDao.findAllById(this.getIds(fieldDefinitions)).isEmpty());
            assertTrue(this.projectDAO.findById(project.getId()).isEmpty());
            
            Organization organizationFromDB = this.organizationDAO.findById(organization.getId()).orElseThrow();
            assertFalse(organizationFromDB.getProjects().contains(project));
        }
    }

    private <T extends BaseEntity> ArrayList<String> getIds(ArrayList<T> entities){
        ArrayList<String> allIds = new ArrayList<>();

        for (T entity : entities){
            allIds.add(entity.getId());
        }

        return allIds;
    }

}

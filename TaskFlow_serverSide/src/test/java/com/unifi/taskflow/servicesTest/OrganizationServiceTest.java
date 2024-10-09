package com.unifi.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.unifi.taskflow.TestUtil;
import com.unifi.taskflow.businessLogic.dtos.OrganizationDTO;
import com.unifi.taskflow.businessLogic.dtos.ProjectDTO;
import com.unifi.taskflow.businessLogic.dtos.UserDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.FieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.SimpleFieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.mappers.OrganizationMapper;
import com.unifi.taskflow.businessLogic.mappers.ProjectMapper;
import com.unifi.taskflow.businessLogic.mappers.UserMapper;
import com.unifi.taskflow.businessLogic.services.OrganizationService;
import com.unifi.taskflow.businessLogic.services.fieldDefinitionServices.FieldDefinitionServiceManager;
import com.unifi.taskflow.daos.ActivityDAO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.OrganizationDAO;
import com.unifi.taskflow.daos.ProjectDAO;
import com.unifi.taskflow.domainModel.Activity;
import com.unifi.taskflow.domainModel.Organization;
import com.unifi.taskflow.domainModel.Project;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.Text;

import net.bytebuddy.utility.RandomString;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class OrganizationServiceTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OrganizationDAO organizationDAO;
    @Autowired
    private FieldDefinitionServiceManager fieldDefinitionServiceManager;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private FieldDAO fieldDao;

    private Organization organizationToTest;

    @BeforeEach
    public void setupDatabase() {
        this.testUtil.cleanDatabase();
        this.organizationToTest = this.testUtil.addRandomOrganizationToDatabase();
    }

    @Test
    public void testCreationOrganization() {

        // creazione organizationDto

        User owner = this.testUtil.addGetRandomUserToDatabase();
        ArrayList<User> owners = new ArrayList<User>();
        owners.add(owner);

        ArrayList<String> ownerIds = new ArrayList<>();
        ownerIds.add(owner.getId());

        OrganizationDTO orgDto = new OrganizationDTO();
        orgDto.setName(RandomString.make(10));
        orgDto.setOwnersId(ownerIds);

        orgDto = organizationService.createNewOrganization(orgDto.getOwnersId().get(0), orgDto);

        Organization orgFromDb = organizationDAO.findById(orgDto.getId()).orElse(null);

        assertEquals(orgDto.getId(), orgFromDb.getId());

    }

    @Test
    public void testAddMemberToOrganization() {
        User user = this.testUtil.addGetRandomUserToDatabase();
        UserDTO userDTO = userMapper.toDto(user);

        OrganizationDTO orgDto = organizationMapper.toDto(this.organizationToTest);
        orgDto = organizationService.addMemberToOrganization(this.organizationToTest.getId(), userDTO.getId());
        Organization found = organizationDAO.findById(orgDto.getId()).orElse(null);

        OrganizationDTO foundDto = organizationMapper.toDto(found);
        for (int i = 0; i < foundDto.getMembersId().size(); i++) {
            assertEquals(orgDto.getMembersId().get(i), foundDto.getMembersId().get(i));
        }
    }

    @Test
    public void testAddOwnerToOrganization() {
        User user = this.testUtil.addGetRandomUserToDatabase();
        UserDTO userDTO = userMapper.toDto(user);

        OrganizationDTO orgDto = organizationMapper.toDto(this.organizationToTest);
        orgDto = organizationService.addOwnerToOrganization(this.organizationToTest.getId(), userDTO.getId());
        Organization found = organizationDAO.findById(orgDto.getId()).orElse(null);

        OrganizationDTO foundDto = organizationMapper.toDto(found);

        for (int i = 0; i < foundDto.getOwnersId().size(); i++) {
            assertEquals(orgDto.getOwnersId().get(i), foundDto.getOwnersId().get(i));
        }
    }

    @Test
    public void testAddProjectToOrganization() {
        Project newProject = this.testUtil.addRandomProjectToDatabase();
        ProjectDTO newProjectDTO = projectMapper.toDto(newProject);

        OrganizationDTO orgDto = organizationMapper.toDto(this.organizationToTest);
        orgDto = organizationService.addNewProjectToOrganization(this.organizationToTest.getId(), newProjectDTO);
        Organization found = organizationDAO.findById(orgDto.getId()).orElse(null);

        OrganizationDTO foundDto = organizationMapper.toDto(found);

        for (int i = 0; i < foundDto.getProjectsId().size(); i++) {
            assertEquals(orgDto.getProjectsId().get(i), foundDto.getProjectsId().get(i));
        }
    }

    @Test
    public void testDeleteOrganization() {

        User user = this.testUtil.addGetRandomUserToDatabase();
        User owner = this.testUtil.addGetRandomUserToDatabase();

        ArrayList<User> users = new ArrayList<User>();
        ArrayList<User> owners = new ArrayList<User>();
        users.add(user);
        owners.add(owner);

        FieldDefinitionDTO simpleFieldDefinitionDTO = new SimpleFieldDefinitionDTO();
        simpleFieldDefinitionDTO.setName("prova");
        simpleFieldDefinitionDTO.setType(FieldType.TEXT);
        FieldDefinition textDefinition = this.fieldDefinitionServiceManager.getFieldDefinitionService(simpleFieldDefinitionDTO).pushNewFieldDefinition(simpleFieldDefinitionDTO);
        ArrayList<FieldDefinition> fieldDefs = new ArrayList<FieldDefinition>();
        fieldDefs.add(textDefinition);

        ArrayList<Field> fields = new ArrayList<Field>();
        Field field = new Text(UUID.randomUUID().toString(), textDefinition, "test");
        fieldDao.save(field);
        fields.add(field);

        ArrayList<Activity> activities = new ArrayList<Activity>();
        Activity activity = new Activity(UUID.randomUUID().toString(), "act", fields);
        activityDAO.save(activity);
        activities.add(activity);

        ArrayList<Project> projects = new ArrayList<Project>();
        Project project = new Project(UUID.randomUUID().toString(), "proj", fieldDefs, activities);
        projectDAO.save(project);
        projects.add(project);

        Organization org = new Organization(UUID.randomUUID().toString(), "org", owners, projects, users,
                LocalDateTime.now());
        
        org = organizationDAO.save(org);


        this.organizationService.deleteOrganization(org.getId());
    }
}

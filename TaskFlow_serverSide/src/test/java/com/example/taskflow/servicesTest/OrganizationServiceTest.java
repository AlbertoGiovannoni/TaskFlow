package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DTOs.Field.StringDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.ActivityMapper;
import com.example.taskflow.Mappers.FieldMapper;
import com.example.taskflow.Mappers.OrganizationMapper;
import com.example.taskflow.Mappers.ProjectMapper;
import com.example.taskflow.Mappers.UserMapper;
import com.example.taskflow.service.ActivityService;
import com.example.taskflow.service.OrganizationService;
import com.example.taskflow.service.FieldService.FieldServiceManager;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class OrganizationServiceTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private FieldServiceManager fieldServiceManager;
    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private FieldMapper fieldMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private NotificationDAO notificationDao;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ActivityDAO activityDao;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private OrganizationDAO organizationDAO;

    private ArrayList<User> someUsers;
    private Organization organizationToTest;

    @BeforeEach
    public void setupDatabase() {
        this.testUtil.cleanDatabase();
        this.someUsers = this.testUtil.addGetMultipleRandomUserToDatabase(5);
        this.organizationToTest = this.testUtil.addRandomOrganizationToDatabase();
    }

    @Test
    public void testCreationOrganization() {
    
        //creazione organizationDto

        User owner = this.testUtil.addGetRandomUserToDatabase();
        ArrayList<User> owners = new ArrayList<User>();
        owners.add(owner);

        Organization org = new Organization("name", owners, new ArrayList<Project>(), new ArrayList<User>(), LocalDateTime.now());

        OrganizationDTO orgDto = organizationMapper.toDto(org);

        orgDto = organizationService.createNewOrganization(orgDto);

        Organization orgFromDb = organizationDAO.findById(orgDto.getId()).orElse(null);

        assertEquals(orgDto.getId(), orgFromDb.getId());

    }

    @Test
    public void testAddMemberToOrganization() {
        User user = this.testUtil.addGetRandomUserToDatabase();
        UserDTO userDTO = userMapper.toDto(user);

        OrganizationDTO orgDto = organizationMapper.toDto(this.organizationToTest);
        orgDto = organizationService.addMemberToOrganization(orgDto, userDTO);
        Organization found = organizationDAO.findById(orgDto.getId()).orElse(null);

        OrganizationDTO foundDto = organizationMapper.toDto(found);
        for(int i=0; i<foundDto.getMembersId().size(); i++){
            assertEquals(orgDto.getMembersId().get(i), foundDto.getMembersId().get(i));
        }
    }

    @Test
    public void testAddOwnerToOrganization() {
        User user = this.testUtil.addGetRandomUserToDatabase();
        UserDTO userDTO = userMapper.toDto(user);

        OrganizationDTO orgDto = organizationMapper.toDto(this.organizationToTest);
        orgDto = organizationService.addOwnerToOrganization(orgDto, userDTO);
        Organization found = organizationDAO.findById(orgDto.getId()).orElse(null);

        OrganizationDTO foundDto = organizationMapper.toDto(found);

        for(int i=0; i<foundDto.getOwnersId().size(); i++){
            assertEquals(orgDto.getOwnersId().get(i), foundDto.getOwnersId().get(i));
        }    
    }
    @Test
    public void testAddProjectToOrganization() {
        Project newProject = this.testUtil.addRandomProjectToDatabase();
        ProjectDTO newProjectDTO = projectMapper.toDto(newProject);

        OrganizationDTO orgDto = organizationMapper.toDto(this.organizationToTest);
        orgDto = organizationService.addNewProjectToOrganization(orgDto, newProjectDTO);
        Organization found = organizationDAO.findById(orgDto.getId()).orElse(null);

        OrganizationDTO foundDto = organizationMapper.toDto(found);

        for(int i=0; i<foundDto.getProjectsId().size(); i++){
            assertEquals(orgDto.getProjectsId().get(i), foundDto.getProjectsId().get(i));
        }    
    }
}

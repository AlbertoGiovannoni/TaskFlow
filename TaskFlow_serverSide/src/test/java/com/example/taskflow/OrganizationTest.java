package com.example.taskflow;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.OrganizationDAO;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class OrganizationTest {

    @Autowired
    private TestUtil testUtil;

    @Autowired
    OrganizationDAO OrganizationDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    ProjectDAO projectDAO;

    @Autowired
    MongoTemplate template;

    ArrayList<User> someUsers;
    ArrayList<User> someOwners;
    ArrayList<Project> someProjects;
    Organization anOrganization;

    @BeforeEach
    public void setupDatabase(){
        //this.testUtil.cleanDatabase();

        this.testUtil.addGetMultipleRandomUserToDatabase(5);
        this.testUtil.addMultipleRandomProjectsToDatabase(5);

        this.someUsers = this.userDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));
        this.someOwners = new ArrayList<>();
        this.someOwners.add(this.someUsers.remove(0));
        this.someProjects = this.projectDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));

        LocalDateTime now = LocalDateTime.now();

        anOrganization = new Organization(UUID.randomUUID().toString(), "i sette nani", someOwners, someProjects, someUsers, now);
        anOrganization = OrganizationDAO.save(anOrganization);
    }

    @Test
    public void testInsertAndFindOrganization() {
        Organization found = OrganizationDAO.findById(anOrganization.getId()).orElse(null);
        assertNotNull(found);
        this.testUtil.checkEqualOrganizations(anOrganization, found);
    }

    @Test
    public void testMondifyOrganization() {
        Organization org_v1 = OrganizationDAO.findById(anOrganization.getId()).orElse(null);

        User newUser = this.testUtil.addGetRandomUserToDatabase(); 
        User newOwner = this.testUtil.addGetRandomUserToDatabase(); 
        Project newProject = this.testUtil.addRandomProjectToDatabase();

        org_v1.setName("i nove nani");
        org_v1.addMember(newUser);
        org_v1.addProject(newProject);
        org_v1.addOwner(newOwner);

        this.OrganizationDAO.save(org_v1);
        Organization org_v2 = OrganizationDAO.findById(anOrganization.getId()).orElse(null);
        
        this.testUtil.checkEqualOrganizations(org_v1, org_v2);
    }
}


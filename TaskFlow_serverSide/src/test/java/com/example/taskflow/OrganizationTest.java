package com.example.taskflow;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;

import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.OrganizationDAO;
import java.util.ArrayList;

import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@DataMongoTest
@ActiveProfiles("test")
public class OrganizationTest {

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
        if (template.collectionExists("fieldDefinition")){
            template.dropCollection("fieldDefinition");
        }
        if (template.collectionExists("user")){
            template.dropCollection("user");
        }
        if (template.collectionExists("userInfo")){
            template.dropCollection("userInfo");
        }
        if (template.collectionExists("project")){
            template.dropCollection("project");
        }
        if (template.collectionExists("organization")){
            template.dropCollection("organization");
        }

        addGetMultipleRandomUserToDatabase(5);
        addMultipleRandomProjectsToDatabase(5);

        this.someUsers = this.userDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));
        this.someOwners = new ArrayList<>();
        this.someOwners.add(this.someUsers.remove(0));
        this.someProjects = this.projectDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String creationDate = now.format(formatter);

        anOrganization = new Organization("i sette nani", someOwners, someProjects, someUsers, creationDate);
        anOrganization = OrganizationDAO.save(anOrganization);
    }

    @Test
    public void testInsertAndFindOrganization() {

        Organization found = OrganizationDAO.findById(anOrganization.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(anOrganization.getName(), found.getName());
        assertEquals(anOrganization.getCreationDate(), found.getCreationDate());

        for(int i=0; i<anOrganization.getOwners().size(); i++)
            assertEquals(anOrganization.getOwners().get(i).getUsername(), found.getOwners().get(i).getUsername());

        for(int i=0; i<anOrganization.getMembers().size(); i++)
            assertEquals(anOrganization.getMembers().get(i).getUsername(), found.getMembers().get(i).getUsername());

        for(int i=0; i<anOrganization.getProjects().size(); i++)
            assertEquals(anOrganization.getProjects().get(i).getName(), found.getProjects().get(i).getName());

        assertEquals(anOrganization.getUuid(), found.getUuid());
    }

    @Test
    public void testMondifyOrganization() {
    }
    
    private User addRandomUserToDatabase(){
        UserInfo info = new UserInfo(RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(info);

        User user = new User(info, RandomString.make(10));
        return this.userDAO.save(user);
    }

    private ArrayList<User> addGetMultipleRandomUserToDatabase(int n){
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < n; i++){
            users.add(this.addRandomUserToDatabase());
        }

        return users;
    }

    private Project addRandomProjectToDatabase(){
        Project project = new Project(RandomString.make(10));
        return this.projectDAO.save(project);
    }

    private ArrayList<Project> addMultipleRandomProjectsToDatabase(int n){
        ArrayList<Project> projects = new ArrayList<>();

        for (int i = 0; i < n; i++){
            projects.add(this.addRandomProjectToDatabase());
        }

        return projects;
    }
}


package com.example.taskflow;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;

import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.OrganizationDAO;
import java.util.ArrayList;

import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class ProjectTest {

    @Autowired
    private TestUtil testUtil;

    @Autowired
    OrganizationDAO OrganizationDAO;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    ActivityDAO activityDAO;

    @Autowired
    ProjectDAO projectDAO;

    @Autowired
    MongoTemplate template;

    ArrayList<Field> someFields;
    ArrayList<Activity> someActivities;
    ArrayList<User> someOwners;
    Project project;
    FieldDefinition fieldDef;

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();        
        this.fieldDef = this.testUtil.pushGetRandomFieldDefinitionToDatabase(FieldType.TEXT);
        this.project = this.testUtil.addRandomProjectToDatabase();
        this.project.addFieldDefinition(fieldDef);
        this.someActivities = this.testUtil.addMultipleRandomActivitiesToDatabase(5);
        this.project.setActivities(someActivities);
        this.projectDAO.save(project);
    }

    @Test
    public void testInsertAndFindProject() {
        Project found = projectDAO.findById(project.getId()).orElse(null);
        assertNotNull(found);
        this.testUtil.checkEqualProject(project, found);
    }

    @Test
    public void testMondifyOrganization() {
        //TODO prendi il progetto e modificalo
    }
}

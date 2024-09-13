package com.example.taskflow;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Field;

import net.bytebuddy.utility.RandomString;

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
import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.OrganizationDAO;
import java.util.ArrayList;

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
        this.fieldDef = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.TEXT);
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
    public void testMondifyProject() {
        Project found1 = projectDAO.findById(project.getId()).orElse(null);
        this.someActivities = this.testUtil.addMultipleRandomActivitiesToDatabase(5);
        found1.setActivities(someActivities);
        found1.setName(RandomString.make(10));
        
        this.projectDAO.save(found1);
        Project found2 = projectDAO.findById(project.getId()).orElse(null);
        this.testUtil.checkEqualProject(found1, found2);
    }
}

package com.example.taskflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldBuilder;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;
import java.util.Random;

import net.bytebuddy.utility.RandomString;

@Component
public class TestUtil {

    @Autowired
    private MongoTemplate template;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private ActivityDAO activityDAO;
    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private OrganizationDAO organizationDAO;

    public void cleanDatabase() {
        Set<String> allCollections = this.template.getCollectionNames();

        for (String collectionName : allCollections) {
            this.template.dropCollection(collectionName);
        }

        
        UserInfo userInfo = new UserInfo("admin@gmail.com", "password");
        userInfoDAO.save(userInfo);

        User admin = new User(userInfo, "admin", true);
        userDAO.save(admin);
    }

    public FieldDefinition pushGetFieldDefinitionToDatabase(FieldType type) {
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(type)
                .setName(RandomString.make(10))
                .build();

        return this.fieldDefinitionDAO.save(fieldDefinition);
    }

    public User addGetRandomUserToDatabase() {
        String plainPassword = "password";

        UserInfo info = new UserInfo(RandomString.make(10), plainPassword);
        this.userInfoDAO.save(info);

        User user = new User(info, RandomString.make(10), false);
        return this.userDAO.save(user);
    }

    public ArrayList<User> addGetMultipleRandomUserToDatabase(int n) {
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            users.add(this.addGetRandomUserToDatabase());
        }

        return users;
    }

    public Activity addRandomActivityToDatabase() {
        Activity activity = new Activity(RandomString.make(10));
        return this.activityDAO.save(activity);
    }

    public ArrayList<Activity> addMultipleRandomActivitiesToDatabase(int n) {
        ArrayList<Activity> activities = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            activities.add(this.addRandomActivityToDatabase());
        }

        return activities;
    }

    public Project addRandomProjectToDatabase() {
        Project project = new Project(RandomString.make(10));
        return this.projectDAO.save(project);
    }

    public ArrayList<Project> addMultipleRandomProjectsToDatabase(int n) {
        ArrayList<Project> projects = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            projects.add(this.addRandomProjectToDatabase());
        }

        return projects;
    }

    public Organization addRandomOrganizationToDatabase() {
        Organization organization = new Organization(RandomString.make(10), new ArrayList<User>(), new ArrayList<Project>(), new ArrayList<User>(), null);
        return this.organizationDAO.save(organization);
    }

    public ArrayList<Field> pushGetNumberFieldsWithSameDefinition(int n){
        Field field;
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(FieldType.NUMBER)
                                            .setName(RandomString.make(10))
                                            .build();

        this.fieldDefinitionDAO.save(fieldDefinition);

        Random randomGenerator = new Random();
        ArrayList<Field> allFieldsGenerated = new ArrayList<>();

        for (int i = 0; i < n; i++){
            field = FieldFactory.getBuilder(FieldType.NUMBER)
                        .addFieldDefinition(fieldDefinition)
                        .addParameter((float)randomGenerator.nextInt(1000))
                        .build();
            
            allFieldsGenerated.add(field);
            this.fieldDao.save(field);
        }

        return allFieldsGenerated;
    }

    public ArrayList<Field> pushGetNumberFields(int n){
        Field field;
        FieldDefinition fieldDefinition;
        Random randomGenerator = new Random();
        ArrayList<Field> allFieldsGenerated = new ArrayList<>();

        for (int i = 0; i < n; i++){
            fieldDefinition = FieldDefinitionFactory.getBuilder(FieldType.NUMBER)
                                .setName(RandomString.make(10))
                                .build();
            field = FieldFactory.getBuilder(FieldType.NUMBER)
                        .addFieldDefinition(fieldDefinition)
                        .addParameter((float)randomGenerator.nextInt(1000))
                        .build();
            
            allFieldsGenerated.add(field);

            this.fieldDefinitionDAO.save(fieldDefinition);
            this.fieldDao.save(field);
        }

        return allFieldsGenerated;
    }

    public void checkEqualOrganizations(Organization o1, Organization o2) {

        assertEquals(o1.getName(), o2.getName());
        assertEquals(o1.getCreationDate(), o2.getCreationDate());

        for (int i = 0; i < o1.getOwners().size(); i++)
            assertEquals(o1.getOwners().get(i).getUsername(), o2.getOwners().get(i).getUsername());

        for (int i = 0; i < o1.getMembers().size(); i++)
            assertEquals(o1.getMembers().get(i).getUsername(), o2.getMembers().get(i).getUsername());

        for (int i = 0; i < o1.getProjects().size(); i++)
            assertEquals(o1.getProjects().get(i).getName(), o2.getProjects().get(i).getName());

        assertEquals(o1.getUuid(), o2.getUuid());
    }

    public void checkEqualActivities(Activity a1, Activity a2) {
        assertEquals(a1.getName(), a2.getName());
        assertEquals(a1.getUuid(), a2.getUuid());
        for (int i = 0; i < a1.getFields().size(); i++) {
            assertEquals(a1.getFields().get(i).getUuid(), a2.getFields().get(i).getUuid());
        }
    }

    public void checkEqualProject(Project p1, Project p2) {
        assertEquals(p1.getName(), p2.getName());
        assertEquals(p1.getUuid(), p2.getUuid());
        for (int i = 0; i < p1.getActivities().size(); i++) {
            assertEquals(p1.getActivities().get(i).getUuid(), p2.getActivities().get(i).getUuid());
        }
    }
    
}

package com.example.taskflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
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

    public void cleanDatabase(){
        Set<String> allCollections = this.template.getCollectionNames();
        
        for (String collectionName : allCollections){
            this.template.dropCollection(collectionName);
        }
    }

    public FieldDefinition pushGetRandomFieldDefinitionToDatabase(FieldType type){
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(type)
                                    .setName(RandomString.make(10))
                                    .build();

        return this.fieldDefinitionDAO.save(fieldDefinition);
    }

    public User addGetRandomUserToDatabase(){
        UserInfo info = new UserInfo(RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(info);

        User user = new User(info, RandomString.make(10));
        return this.userDAO.save(user);
    }

    public ArrayList<User> addGetMultipleRandomUserToDatabase(int n){
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < n; i++){
            users.add(this.addGetRandomUserToDatabase());
        }

        return users;
    }

    public Activity addRandomActivityToDatabase(){
        Activity activity = new Activity(RandomString.make(10));
        return this.activityDAO.save(activity);
    }

    public ArrayList<Activity> addMultipleRandomActivitiesToDatabase(int n){
        ArrayList<Activity> activities = new ArrayList<>();

        for (int i = 0; i < n; i++){
            activities.add(this.addRandomActivityToDatabase());
        }

        return activities;
    }

    public Project addRandomProjectToDatabase(){
        Project project = new Project(RandomString.make(10));
        return this.projectDAO.save(project);
    }

    public ArrayList<Project> addMultipleRandomProjectsToDatabase(int n){
        ArrayList<Project> projects = new ArrayList<>();

        for (int i = 0; i < n; i++){
            projects.add(this.addRandomProjectToDatabase());
        }

        return projects;
    }

    public void checkEqualOrganizations(Organization o1, Organization o2){

        assertEquals(o1.getName(), o2.getName());
        assertEquals(o1.getCreationDate(), o2.getCreationDate());

        for(int i=0; i<o1.getOwners().size(); i++)
            assertEquals(o1.getOwners().get(i).getUsername(), o2.getOwners().get(i).getUsername());

        for(int i=0; i<o1.getMembers().size(); i++)
            assertEquals(o1.getMembers().get(i).getUsername(), o2.getMembers().get(i).getUsername());

        for(int i=0; i<o1.getProjects().size(); i++)
            assertEquals(o1.getProjects().get(i).getName(), o2.getProjects().get(i).getName());

        assertEquals(o1.getUuid(), o2.getUuid());
    }

    public void checkEqualProject(Project p1, Project p2){
        assertEquals(p1.getName(), p2.getName());
        assertEquals(p1.getUuid(), p2.getUuid());
        for(int i=0; i<p1.getActivities().size(); i++){
            assertEquals(p1.getActivities().get(i).getUuid(), p2.getActivities().get(i).getUuid());
        }
    }
}

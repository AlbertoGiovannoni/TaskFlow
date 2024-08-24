package com.example.taskflow;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;

import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;


@DataMongoTest
@ActiveProfiles("test")
public class OrganizationTest {

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserInfoDAO userInfoDAO;

    @Autowired
    private FieldDefinitionDAO fieldDefinitionDAO;

    @Autowired
    private ActivityDAO activityDAO;

    @Autowired
    private ProjectDAO projectDAO;

    private Project createRandomProject(){

        ArrayList<FieldDefinition> fieldsTemplate = new ArrayList<FieldDefinition>();
        ArrayList<Activity> activities = new ArrayList<Activity>();
        ArrayList<Field> fields = new ArrayList<Field>();

        FieldDefinition fieldDefinition = FieldDefinitionBuilder.buildField(FieldType.DATE, RandomString.make(5));
        fieldDefinition = fieldDefinitionDAO.save(fieldDefinition);
        fieldsTemplate.add(fieldDefinition);

        Field<LocalDateTime> date = new Date(LocalDateTime.now(), fieldDefinition);
        fields.add(date);

        Activity activity = new Activity("test1", fields);
        activity = activityDAO.save(activity);

        activities.add(activity);

        return new Project(RandomString.make(10), fieldsTemplate, activities);
    }

    @Test
    public void testInsertAndFindOrganization() {
        
        ArrayList<Project> projects = new ArrayList<Project>();
        ArrayList<User> members = new ArrayList<User>();
        ArrayList<User> owners = new ArrayList<>();

        // creo e salvo userInfo per l'owner
        UserInfo uiOwner = new UserInfo("paolo.rossi@gmail.com", "password");
        userInfoDAO.save(uiOwner);

        UserInfo uiUser = new UserInfo("simone.bianchi@gmail.com", "psw");
        userInfoDAO.save(uiUser);

        // creo user e lo salvo
        User owner = new User(uiOwner, "owner215");
        owner = userDAO.save(owner);

        owners.add(owner);
        members.add(owner);

        User member = new User(uiUser, "member123");
        owner = userDAO.save(member);

        members.add(member);

        // creazione progetti

        Project project = this.createRandomProject();
        projectDAO.save(project);

        projects.add(project);

        Organization organization = new Organization("Example Organization", owners, projects, members);

        // salvo organizzazione
        organization = organizationDAO.save(organization);

        Organization found = organizationDAO.findById(organization.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(organization.getName(), found.getName());
        assertEquals(organization.getUuid(), found.getUuid());
        assertEquals(1, found.getOwners().size());
    }
}
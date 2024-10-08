package com.unifi.taskflow.businessLogic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.daos.ActivityDAO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.OrganizationDAO;
import com.unifi.taskflow.daos.ProjectDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.Activity;
import com.unifi.taskflow.domainModel.BaseEntity;
import com.unifi.taskflow.domainModel.Organization;
import com.unifi.taskflow.domainModel.Project;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Field;

@Service
public class CheckUriService {
    @Autowired
    UserDAO userDao;
    @Autowired
    OrganizationDAO organizationDao;
    @Autowired
    ProjectDAO projectDao;
    @Autowired
    ActivityDAO activityDao;
    @Autowired
    FieldDAO fieldDao;
    @Autowired
    FieldDefinitionDAO fieldDefinitionDao;

    User user;
    Organization organization;
    Project project;
    Activity activity;
    Field field;
    FieldDefinition fieldDefinition;

    public boolean check(Authentication authentication, String userId){
        User userFromAuthentication = this.userDao.findByUsername(authentication.getName()).orElse(null);

        this.checkNullOrThrow(userFromAuthentication, authentication.getName());

        if (!(userFromAuthentication.getId().equals(userId))){
            throw new IllegalArgumentException(userId + " doesn't match authentication username");
        }

        this.user = userFromAuthentication;

        return true;
    }

    public boolean check(Authentication authentication, String userId, String organizationId){
        this.check(authentication, userId);

        Organization organization = this.organizationDao.findById(organizationId).orElse(null);

        this.checkNullOrThrow(organization, organizationId);

        if (!(organization.getUsers().contains(this.user))){
            throw new IllegalArgumentException(this.user.getUsername() + " is not in organization " + organizationId);
        }

        this.organization = organization;

        return true;
    }

    public boolean check(Authentication authentication, String userId, String organizationId, String projectId){
        this.check(authentication, userId, organizationId);

        Project project = this.projectDao.findById(projectId).orElse(null);
        this.checkNullOrThrow(project, projectId);

        if (!(this.organization.getProjects().contains(project))){
            throw new IllegalArgumentException("Project " + projectId + " isn't in organization " + organizationId);
        }

        this.project = project;

        return true;
    }

    public boolean check(Authentication authentication, String userId, String organizationId, String projectId, String activityId){
        this.check(authentication, userId, organizationId, projectId);

        Activity activity = this.activityDao.findById(activityId).orElse(null);
        this.checkNullOrThrow(activity, projectId);

        if (!(this.project.getActivities().contains(activity))){
            throw new IllegalArgumentException("Activity " + activityId + " isn't in project " + projectId);
        }

        this.activity = activity;

        return true;
    }

    public boolean check(Authentication authentication, String userId, String organizationId, String projectId, String activityId, String fieldId){
        this.check(authentication, userId, organizationId, projectId, activityId);

        Field field = this.fieldDao.findById(fieldId).orElse(null);
        this.checkNullOrThrow(field, fieldId);

        if (!(this.activity.getFields().contains(field))){
            throw new IllegalArgumentException("Field " + fieldId + " isn't in activity " + activityId);
        }

        this.field = field;

        return true;
    }

    public boolean checkFieldDefinition(Authentication authentication, String userId, String organizationId, String projectId, String fieldDefinitionId){
        this.check(authentication, userId, organizationId, projectId);

        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElse(null);
        this.checkNullOrThrow(fieldDefinition, fieldDefinitionId);

        if (!(this.project.getFieldsTemplate().contains(fieldDefinition))){
            throw new IllegalArgumentException("Project " + projectId + " isn't in organization " + organizationId);
        }

        this.fieldDefinition = fieldDefinition;

        return true;
    }

    private <T extends BaseEntity> void checkNullOrThrow(T object, String objectName){
        if (object == null){
            throw new IllegalArgumentException(objectName + " not found");
        }
    }
}

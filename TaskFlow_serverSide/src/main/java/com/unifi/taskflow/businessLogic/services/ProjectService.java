package com.unifi.taskflow.businessLogic.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.ActivityDTO;
import com.unifi.taskflow.businessLogic.dtos.ProjectDTO;
import com.unifi.taskflow.businessLogic.dtos.ProjectSimpleDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.FieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.mappers.ActivityMapper;
import com.unifi.taskflow.businessLogic.mappers.FieldDefinitionMapper;
import com.unifi.taskflow.businessLogic.mappers.FieldMapper;
import com.unifi.taskflow.businessLogic.mappers.ProjectMapper;
import com.unifi.taskflow.businessLogic.services.fieldDefinitionServices.FieldDefinitionServiceManager;
import com.unifi.taskflow.daos.ActivityDAO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.OrganizationDAO;
import com.unifi.taskflow.daos.ProjectDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.Activity;
import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.Organization;
import com.unifi.taskflow.domainModel.Project;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Field;

@Service
public class ProjectService {

    @Autowired
    ProjectDAO projectDao;
    @Autowired
    ActivityDAO activityDao;
    @Autowired
    UserDAO userDao;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ActivityService activityService;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    FieldMapper fieldMapper;
    @Autowired
    FieldDAO fieldDAO;
    @Autowired
    FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    FieldDefinitionServiceManager fieldDefinitionServiceManager;
    @Autowired
    OrganizationDAO organizationDao;
    @Autowired
    FieldDefinitionMapper fieldDefinitionMapper;

    public ProjectDTO pushNewProject(ProjectDTO projectDto) {
        Project project = EntityFactory.getProject();

        if (projectDto.getName() == null){
            throw new IllegalAccessError("Name of project cannot be null");
        }
        
        project.setName(projectDto.getName());
        
        project = this.projectDao.save(project);

        return projectMapper.toDto(project);
    }

    public FieldDefinitionDTO addFieldDefinitionToProject(String projectId, FieldDefinitionDTO newFieldDefinitionDto) {

        Project project = this.projectDao.findById(projectId).orElse(null);

        if (project == null){
            throw new IllegalArgumentException("Project not found");
        }

        FieldDefinition newFieldDefinition = this.fieldDefinitionServiceManager
                .getFieldDefinitionService(newFieldDefinitionDto)
                .pushNewFieldDefinition(newFieldDefinitionDto);
        project.addFieldDefinition(newFieldDefinition);
        this.projectDao.save(project);

        return this.fieldDefinitionMapper.toDto(newFieldDefinition);
    }

    public ActivityDTO addActivityToProject(String projectId, ActivityDTO newActivityDto) {

        Project project = this.projectDao.findById(projectId).orElse(null);

        if (project == null){
            throw new IllegalArgumentException("Project not found");
        }

        Activity newActivity = this.activityService.pushNewActivity(newActivityDto);
        newActivity = this.activityDao.save(newActivity);
        project.addActivity(newActivity);
        this.projectDao.save(project);

        return activityMapper.toDto(newActivity);
    }

    public ProjectDTO getProjectById(String projectId) {
        Project project = this.projectDao.findById(projectId).orElse(null);

        if (project == null){
            throw new IllegalArgumentException("Project not found");
        }

        return this.projectMapper.toDto(project);
    }

    public ProjectSimpleDTO renameProject(String projectId, String newName) {
        if (newName == null){
            throw new IllegalArgumentException("New name must be non null");
        }

        if (newName.isBlank()){
            throw new IllegalArgumentException("New name must be not blank");
        }

        Project project = this.projectDao.findById(projectId).orElse(null);

        if (project == null){
            throw new IllegalArgumentException("Project not found");
        }
        
        if (!(newName.isBlank())){
            project.setName(newName);
            this.projectDao.save(project);
        }
        

        return this.projectMapper.toSimpleDTO(project);
    }

    public void deleteProject(String projectId) {
        Project project = this.projectDao.findById(projectId).orElse(null);

        if (project == null){
            throw new IllegalArgumentException("Project not found");
        }

        ArrayList<Activity> activityList = project.getActivities();
        ArrayList<Field> fields = new ArrayList<>();

        if (activityList != null && activityList.size() > 0) {
            for (Activity activity : activityList) {
                fields.addAll(activity.getFields());
            }
        }

        if (fields != null){
            this.fieldDAO.deleteAll(fields);
        }

        if (activityList != null && activityList.size() > 0) {
            this.activityDao.deleteAll(activityList);
        }

        ArrayList<FieldDefinition> fieldsTemplate = project.getFieldsTemplate();

        if (fieldsTemplate != null) {
            this.fieldDefinitionDao.deleteAll(fieldsTemplate);
        }

        Organization organization = this.organizationDao.getOrganizationByProject(projectId);
        organization.removeProject(project);
        this.organizationDao.save(organization);

        this.projectDao.delete(project);
    }
}

package com.example.taskflow.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.ProjectSimpleDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.EntityFactory;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.ActivityMapper;
import com.example.taskflow.Mappers.FieldDefinitionMapper;
import com.example.taskflow.Mappers.FieldMapper;
import com.example.taskflow.Mappers.ProjectMapper;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionServiceManager;

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

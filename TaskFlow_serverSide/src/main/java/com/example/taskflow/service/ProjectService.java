package com.example.taskflow.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.ActivityMapper;
import com.example.taskflow.Mappers.FieldMapper;
import com.example.taskflow.Mappers.ProjectMapper;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionService;


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
    FieldDefinitionService fieldDefinitionService;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    FieldMapper fieldMapper;

    public ProjectDTO createProject(ProjectDTO projectDto){
        Project project = projectMapper.toEntity(projectDto);
        
        ArrayList<Activity> activities = new ArrayList<Activity>();
        project.setActivities(activities);
        this.projectDao.save(project);

        return projectMapper.toDto(project);
    }

    private Project getProject(ProjectDTO projectDto){

        Project project = projectMapper.toEntity(projectDto);
        
        ArrayList<Activity> activities = new ArrayList<Activity>();
        for(ActivityDTO activityDto:projectDto.getActivities()){
            activities.add(this.activityDao.findById(activityDto.getId()).orElseThrow());
        }
        project.setActivities(activities);
        return project;
    }
    
    public ProjectDTO addFieldDefinitionToProject(String projectId, FieldDefinitionDTO newFieldDefinitionDto){

        Project project = this.projectDao.findById(projectId).orElseThrow();
        FieldDefinition newFieldDefinition = this.fieldDefinitionService.pushNewFieldDefinition(newFieldDefinitionDto);
        project.addFieldDefinition(newFieldDefinition);
        this.projectDao.save(project);

        return projectMapper.toDto(project);
    }

    public ProjectDTO addActivityToProject(String projectId, ActivityDTO newActivityDto){

        Project project = this.projectDao.findById(projectId).orElseThrow();

        Activity newActivity = this.activityService.pushNewActivity(newActivityDto);

        project.addActivity(newActivity);

        this.projectDao.save(project);

        return projectMapper.toDto(project);
    }
    
    public ProjectDTO getProjectById(String projectId){
        Project project = this.projectDao.findById(projectId).orElseThrow();
        return this.projectMapper.toDto(project);
    }

    public ProjectDTO renameProject(String projectId, String newName){
        Project project = this.projectDao.findById(projectId).orElseThrow();
        project.setName(newName);
        this.projectDao.save(project);
        return this.projectMapper.toDto(project);
    }

    public void deleteProject(String projectId){
        Project project = this.projectDao.findById(projectId).orElseThrow();

        for(Activity activity : project.getActivities()){
            activityService.deleteActivityAndFields(activity.getId());           
        }

        this.projectDao.delete(project);
    }
}

package com.example.taskflow.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.Mappers.ActivityMapper;
import com.example.taskflow.Mappers.ProjectMapper;


@Service
public class ProjectService {

    @Autowired
    ProjectDAO projectDao;
    @Autowired
    UserDAO userDao;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ActivityService activityService;
    @Autowired
    ActivityMapper activityMapper;

    public ProjectDTO createProject(ProjectDTO projectDto){
        Project project = projectMapper.toEntity(projectDto);
        
        ArrayList<Activity> activities = new ArrayList<Activity>();
        project.setActivities(activities);
        this.projectDao.save(project);

        return projectMapper.toDto(project);
    }
    
    public void deleteProject(ProjectDTO projectDto){
        Project project = this.projectDao.findById(projectDto.getId()).orElseThrow();
        for(Activity activity:project.getActivities()){
            activityService.deleteActivity(activityMapper.toDto(activity));           
        }
        this.projectDao.delete(project);
    }
}

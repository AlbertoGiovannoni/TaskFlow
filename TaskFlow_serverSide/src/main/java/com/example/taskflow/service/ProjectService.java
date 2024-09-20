package com.example.taskflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;


@Service
public class ProjectService {

    @Autowired
    ProjectDAO projectDao;

    @Autowired
    UserDAO userDao;

    // public void delete(ProjectDTO projectDto){
    //     Project project = this.projectDao.findById(projectDto.getId()).orElseThrow();

    //     //TODO cascading

    //     this.projectDao.delete(project);
    // }
}

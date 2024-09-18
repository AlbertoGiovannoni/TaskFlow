package com.example.taskflow.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DomainModel.Project;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    default ProjectDTO toDto(Project project) {
        ProjectDTO projectDTO = new ProjectDTO(project.getName(), project.getId(), project.getFieldsTemplate(), project.getAllActivities());
        return projectDTO;
    };

    default Project toEntity(ProjectDTO projectDto) {
        Project project = new Project(projectDto.getId(), projectDto.getName(), projectDto.getFieldsTemplate(), projectDto.getActivities());
        return project;
    }
}

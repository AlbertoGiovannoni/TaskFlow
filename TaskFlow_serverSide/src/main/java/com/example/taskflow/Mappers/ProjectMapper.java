package com.example.taskflow.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.ArrayList;

import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.FieldDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class ProjectMapper {

    ProjectDTO toDto(Project project) {

        ActivityMapper activityMapper = new ActivityMapper();
        ArrayList<ActivityDTO> activities = new ArrayList<ActivityDTO>();
        for (Activity activity : project.getActivities()) {
            activities.add(activityMapper.toDto(activity));
        }

        ArrayList<FieldDefinitionDTO> template = new ArrayList<FieldDefinitionDTO>();
        for (FieldDefinition fieldDefinition : project.getFieldsTemplate()) {
            //TODO: converti fieldDefinition in fieldDefinitionDTO e aggiungilo alla lista template
        }

        ProjectDTO projectDTO = new ProjectDTO(project.getId(), project.getName(), template, activities);
        return projectDTO;
    };

    Project toEntity(ProjectDTO projectDto) {

        ArrayList<Activity> activities = new ArrayList<Activity>();
        for (ActivityDTO activity : projectDto.getActivities()) {
            //TODO: converti activity in ActivityDTO e aggiungilo alla lista activities
        }

        ArrayList<FieldDefinition> template = new ArrayList<FieldDefinition>();
        for (FieldDefinitionDTO fieldDefinition : projectDto.getFieldsTemplate()) {
            //TODO: converti fieldDefinition in fieldDefinitionDTO e aggiungilo alla lista template
        }

        Project project = new Project(projectDto.getId(), projectDto.getName(), template, activities);
        return project;
    }
}

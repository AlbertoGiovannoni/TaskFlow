package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.ProjectSimpleDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.BaseEntity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
@Component
public interface ProjectMapper {

    @Mapping(source = "fieldsTemplate", target = "fieldsTemplate", ignore = true)
    @Mapping(source = "activities", target = "activities", ignore = true, qualifiedByName = "getAllActivities")
    Project toEntity(ProjectDTO dto);

    @Mapping(source = "fieldsTemplate", target = "fieldsTemplate", qualifiedByName = "mapFieldsDefinitionToFieldDefinitionDTO")
    @Mapping(source = "activities", target = "activities", qualifiedByName = "mapActivitiesToActivitiesDTO")
    ProjectDTO toDto(Project dto);

    @Mapping(source = "fieldsTemplate", target = "fieldTemplateIds", qualifiedByName = "mapFieldsTemplateWithIds")
    @Mapping(source = "activities", target = "activityIds", qualifiedByName = "mapActivitiesWithIds")
    ProjectSimpleDTO toSimpleDTO(Project project);

    @Named("mapFieldsDefinitionToFieldDefinitionDTO")
    default ArrayList<FieldDefinitionDTO> mapFieldsDefinitionToFieldDefinitionDTO(ArrayList<FieldDefinition> fieldDefs) {
        ArrayList<FieldDefinitionDTO> fieldsDTO = new ArrayList<FieldDefinitionDTO>();
        
        if (fieldDefs != null){
            fieldsDTO = fieldDefinitionsToDto(fieldDefs);
        }

        return fieldsDTO;
    }

    @Named("mapActivitiesToActivitiesDTO")
    default ArrayList<ActivityDTO> mapActivitiesToActivitiesDTO(ArrayList<Activity> activities) {
        ArrayList<ActivityDTO> activitiesDTO = new ArrayList<ActivityDTO>();

        if (activities != null){
            activitiesDTO = activityToDto(activities);
        }

        return activitiesDTO;
    }

    @Named("mapFieldsTemplateWithIds")
    default ArrayList<String> mapFieldsTemplateWithIds(ArrayList<FieldDefinition> fieldDefinitions){
        return this.getIds(fieldDefinitions);
    }

    @Named("mapActivitiesWithIds")
    default ArrayList<String> mapActivitiesWithIds(ArrayList<Activity> activities){
        return this.getIds(activities);
    }

    default ArrayList<FieldDefinitionDTO> fieldDefinitionsToDto(ArrayList<FieldDefinition> fieldDefinitions) {
        return Mappers.getMapper(FieldDefinitionMapper.class).mapFieldsDefinitionToFieldDefinitionDTO(fieldDefinitions);
    }

    default ArrayList<ActivityDTO> activityToDto(ArrayList<Activity> activities) {
        return Mappers.getMapper(ActivityMapper.class).activityToActivityDto(activities);
    }

    default <T extends BaseEntity> ArrayList<String> getIds(ArrayList<T> objects){
        ArrayList<String> ids = new ArrayList<>();

        if (objects != null){
            for (T object : objects){
                ids.add(object.getId());
            }
        }

        return ids;
    }
}

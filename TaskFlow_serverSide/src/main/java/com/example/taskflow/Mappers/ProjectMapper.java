package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
@Component
public interface ProjectMapper {

    public static final ActivityMapper activityMapper = null;                   //FIXME          
    public static final FieldDefinitionMapper fieldDefinitionMapper = null;     //FIXME

    @Mapping(source = "fieldsTemplate", target = "fieldsTemplate", ignore = true)
    @Mapping(source = "activities", target = "activities", ignore = true, qualifiedByName = "getAllActivities")
    Project toEntity(ProjectDTO dto);

    @Named("mapFieldsDefinitionToFieldDefinitionDTO")
    default ArrayList<FieldDefinitionDTO> mapFieldsDefinitionToFieldDefinitionDTO(ArrayList<FieldDefinition> fieldDefs) {
        ArrayList<FieldDefinitionDTO> fieldsDTO = new ArrayList<FieldDefinitionDTO>();
        for(FieldDefinition fieldDef:fieldDefs){
            fieldsDTO.add(fieldDefinitionMapper.toDto(fieldDef));
        }
        return fieldsDTO;
    }

    @Named("mapActivitiesToActivitiesDTO")
    default ArrayList<ActivityDTO> mapActivitiesToActivitiesDTO(ArrayList<Activity> activities) {
        ArrayList<ActivityDTO> activitiesDTO = new ArrayList<ActivityDTO>();
        for(Activity activity:activities){
            activitiesDTO.add(activityMapper.toDto(activity));
        }
        return activitiesDTO;
    }
    @Mapping(source = "fieldsTemplate", target = "fieldsTemplate", qualifiedByName = "mapFieldsDefinitionToFieldDefinitionDTO")
    @Mapping(source = "activities", target = "activities", qualifiedByName = "mapActivitiesToActivitiesDTO")
    ProjectDTO toDto(Project dto);
}

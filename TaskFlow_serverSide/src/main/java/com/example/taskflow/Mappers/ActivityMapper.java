package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.FieldPackage.Field;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import java.util.ArrayList;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class ActivityMapper {

    ActivityDTO toDto(Activity activity) {
        ArrayList<FieldDTO> fields = new ArrayList<FieldDTO>();
        for (Field field : activity.getFields()) {
            //TODO: converti field in fieldDTO e aggiungilo alla lista fields
        }
        ActivityDTO activityDTO = new ActivityDTO(activity.getName(), activity.getId(), fields);
        return activityDTO;
    };

    Activity toEntity(ActivityDTO activityDto) {
        
        ArrayList<Field> fields = new ArrayList<Field>();
        for (FieldDTO fieldDto : activityDto.getFields()) {
            //TODO: converti fieldDTO in field e aggiungilo alla lista fields
        }
        Activity activity = new Activity(activityDto.getId(), activityDto.getName(), fields);
        activity.setId(activityDto.getId());
        return activity;
    }
}



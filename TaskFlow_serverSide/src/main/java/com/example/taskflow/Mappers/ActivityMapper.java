package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DomainModel.Activity;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ActivityMapper {

    default ActivityDTO toDto(Activity activity) {
        ActivityDTO activityDTO = new ActivityDTO(activity.getName(), activity.getId(), activity.getFields());
        return activityDTO;
    };

    default Activity toEntity(ActivityDTO activityDto) {
        Activity activity = new Activity(activityDto.getName(), activityDto.getFields());
        activity.setId(activityDto.getId());
        return activity;
    }
}



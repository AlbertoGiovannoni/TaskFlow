package com.unifi.taskflow.businessLogic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import com.unifi.taskflow.businessLogic.dtos.ActivityDTO;
import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.domainModel.Activity;
import com.unifi.taskflow.domainModel.fields.Field;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
@Component
public interface ActivityMapper {

    @Mapping(source = "fields", target = "fields", ignore = true)
    Activity toEntity(ActivityDTO dto);

    @Mapping(source = "fields", target = "fields", qualifiedByName = "mapFieldsToFieldDTO")
    ActivityDTO toDto(Activity user);

    @Named("mapFieldsToFieldDTO")
    default ArrayList<FieldDTO> mapFieldsToFieldDTO(ArrayList<Field> fields) {
        ArrayList<FieldDTO> fieldsDTO = new ArrayList<FieldDTO>();
        fieldsDTO = fieldToFieldDto(fields);

        return fieldsDTO;
    }

    default ArrayList<FieldDTO> fieldToFieldDto(ArrayList<Field> fields) {
        return Mappers.getMapper(FieldMapper.class).fieldToFieldDto(fields);
    }

    default ArrayList<ActivityDTO> activityToActivityDto(ArrayList<Activity> activities){
        ArrayList<ActivityDTO> activityDtoList = new ArrayList<ActivityDTO>();

        for(Activity activity : activities){
            activityDtoList.add(this.toDto(activity));
        }

        return activityDtoList;
    }
}




package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.FieldPackage.Field;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.taskflow.Mappers.FieldMapper;
import org.mapstruct.factory.Mappers;


import java.util.ArrayList;

@Mapper(componentModel = "spring")
@Component
public interface ActivityMapper {

    @Mapping(source = "fields", target = "fields", ignore = true)
    Activity toEntity(ActivityDTO dto);

    @Named("mapFieldsToFieldDTO")
    default ArrayList<FieldDTO> mapFieldsToFieldDTO(ArrayList<Field> fields) {
        ArrayList<FieldDTO> fieldsDTO = new ArrayList<FieldDTO>();
        fieldsDTO = fieldToFieldDto(fields);

        // for(Field field:fields){
        //     fieldsDTO.add(INSTANCE.toDto(field));
        // }
        return fieldsDTO;
    }

    default ArrayList<FieldDTO> fieldToFieldDto(ArrayList<Field> fields) {
        return Mappers.getMapper(FieldMapper.class).fieldToFieldDto(fields);
    }

    @Mapping(source = "fields", target = "fields", qualifiedByName = "mapFieldsToFieldDTO")
    ActivityDTO toDto(Activity user);
}




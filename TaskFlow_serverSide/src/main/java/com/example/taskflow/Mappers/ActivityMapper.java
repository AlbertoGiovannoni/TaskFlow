package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.FieldPackage.Field;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Mapper(componentModel = "spring")
@Component
public interface ActivityMapper {

    public static final FieldMapper mapper = null;

    @Mapping(source = "fields", target = "fields", ignore = true)
    Activity toEntity(ActivityDTO dto);

    @Named("mapFieldsToFieldDTO")
    default ArrayList<FieldDTO> mapFieldsToFieldDTO(ArrayList<Field> fields) {
        ArrayList<FieldDTO> fieldsDTO = new ArrayList<FieldDTO>();
        for(Field field:fields){
            fieldsDTO.add(mapper.toDto(field));
        }
        return fieldsDTO;
    }

    @Mapping(source = "fields", target = "fields", qualifiedByName = "mapFieldsToFieldDTO")
    ActivityDTO toDto(Activity user);
}




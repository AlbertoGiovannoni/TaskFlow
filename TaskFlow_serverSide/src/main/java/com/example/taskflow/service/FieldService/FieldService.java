package com.example.taskflow.service.FieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.FieldMapper;

@Service
public abstract class FieldService {

    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;

    @Autowired
    FieldMapper fieldMapper;

    abstract public FieldDTO createField(FieldDTO fieldDTO);
    
    public Field getField(FieldDTO fieldDto){
        FieldDefinition fieldDefinition = this.fieldDefinitionDAO.findById(fieldDto.getFieldDefinitionId()).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition isn't in the database");
        }

        Field field = this.fieldMapper.toEntity(fieldDto);
        field.setFieldDefinition(fieldDefinition);

        return field;
    }

}

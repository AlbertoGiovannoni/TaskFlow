package com.example.taskflow.service.FieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;
import com.example.taskflow.Mappers.FieldMapper;

@Service
public class NumberService extends FieldService{

    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    FieldMapper fieldMapper;
    @Autowired
    FieldDAO fieldDao; 
    @Autowired
    UserDAO userDAO;

    @Override
    public FieldDTO createField(FieldDTO fieldDto) {
        if (!(fieldDto instanceof NumberDTO)){
                throw new IllegalArgumentException("FieldDto of class " + fieldDto.getClass().getSimpleName() + " instead of NumberDTO");
        }

        NumberDTO numberDTO = (NumberDTO) fieldDto;

        FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(numberDTO.getFieldDefinitionId())
                .orElse(null);

        if (fieldDefinition == null) {
            throw new IllegalArgumentException("Wrong fieldDefinition id");
        }

        if (!(fieldDefinition instanceof SimpleFieldDefinition)){
            throw new IllegalArgumentException("Wrong fieldDefinition type");
        }

        Field field = FieldFactory.getBuilder(FieldType.NUMBER)
                .addFieldDefinition(fieldDefinition)
                .addParameter(numberDTO.getValue())
                .build();

        field = fieldDao.save(field);

        return fieldMapper.toDto(field);
    }
    
}

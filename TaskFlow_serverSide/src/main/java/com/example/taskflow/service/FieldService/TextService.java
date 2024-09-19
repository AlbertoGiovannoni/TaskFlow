package com.example.taskflow.service.FieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.StringDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;
import com.example.taskflow.Mappers.FieldMapper;
import java.util.ArrayList;

@Service
public class TextService extends FieldService {

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

        if (!(fieldDto instanceof StringDTO)) {
            throw new IllegalArgumentException(
                    "FieldDto of class " + fieldDto.getClass().getSimpleName() + " instead of StringDTO");
        }

        StringDTO stringDTO = (StringDTO) fieldDto;

        FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(stringDTO.getFieldDefinitionId())
                .orElse(null);

        if (fieldDefinition == null) {
            throw new IllegalArgumentException("Wrong fieldDefinition id");
        }

        FieldType fieldType = fieldDefinition.getType();

        Field f = fieldMapper.toEntity(fieldDto);
        f.setFieldDefinition(fieldDefinition);

        ArrayList<String> value = stringDTO.getValuesDto();

        f.setValues(value);

        Field field = FieldFactory.getBuilder(fieldType)
                .addFieldDefinition(fieldDefinition)
                .addParameters(f.getValues())
                .build();

        field = fieldDao.save(field);

        return fieldMapper.toDto(field);
    }
}

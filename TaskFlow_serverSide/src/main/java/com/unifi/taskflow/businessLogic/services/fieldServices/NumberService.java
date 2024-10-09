package com.unifi.taskflow.businessLogic.services.fieldServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.dtos.field.NumberDTO;
import com.unifi.taskflow.businessLogic.mappers.FieldMapper;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.SimpleFieldDefinition;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.Number;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.NumberBuilder;

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
    public Field pushNewField(FieldDTO fieldDto) {
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

        Field field = (new NumberBuilder(fieldDefinition)
                .addParameter(numberDTO.getValue()))
                .build();

        field = fieldDao.save(field);

        return field;
    }

    @Override
    public Field updateField(FieldDTO fieldDto) {
        Number field = (Number) this.fieldDao.findById(fieldDto.getId()).orElse(null);

        if (field == null){
            throw new IllegalArgumentException("Field number not found");
        }

        NumberDTO dateDTO = (NumberDTO) fieldDto;

        field.setValue(dateDTO.getValue());

        this.fieldDao.save(field);

        return field;
    }
    
}

package com.unifi.taskflow.businessLogic.services.fieldServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.dtos.field.SingleSelectionDTO;
import com.unifi.taskflow.businessLogic.mappers.FieldMapper;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.SingleSelection;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.SingleSelectionBuilder;

@Service
public class SingleSelectionService extends FieldService{

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

        if (!(fieldDto instanceof SingleSelectionDTO)) {
            throw new IllegalArgumentException(
                "FieldDto of class " + fieldDto.getClass().getSimpleName() + " instead of StringDTO"
            );
        }

        SingleSelectionDTO stringDTO = (SingleSelectionDTO) fieldDto;

        FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(stringDTO.getFieldDefinitionId())
                .orElse(null);

        if (fieldDefinition == null) {
            throw new IllegalArgumentException("Wrong fieldDefinition id");
        }

        Field field = (new SingleSelectionBuilder(fieldDefinition))
                .addSelection(stringDTO.getValue())
                .build();

        field = fieldDao.save(field);

        return field;
    }


    @Override
    public Field updateField(FieldDTO fieldDto) {
        
        SingleSelection field = (SingleSelection) this.fieldDao.findById(fieldDto.getId()).orElse(null);

        if (field == null){
            throw new IllegalArgumentException("Field single selection not found");
        }

        SingleSelectionDTO singleSelectionDTO = (SingleSelectionDTO) fieldDto;

        field.setValue(singleSelectionDTO.getValue());

        this.fieldDao.save(field);

        return field;
    }
}
package com.example.taskflow.service.FieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.SingleSelectionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.SingleSelection;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.SingleSelectionBuilder;
import com.example.taskflow.Mappers.FieldMapper;

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
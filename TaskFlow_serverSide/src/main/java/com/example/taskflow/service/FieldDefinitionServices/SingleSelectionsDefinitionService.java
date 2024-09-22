package com.example.taskflow.service.FieldDefinitionServices;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SingleSelectionDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;

@Service
public class SingleSelectionsDefinitionService extends FieldDefinitionService{

    @Override
    public FieldDefinition pushNewFieldDefinition(FieldDefinitionDTO fieldDefinitionDto) {
        if (fieldDefinitionDto == null){
            throw new IllegalArgumentException("FieldDefinitionDTO is null");
        }
        if (!(fieldDefinitionDto instanceof SingleSelectionDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinitionDto of class " + fieldDefinitionDto.getClass().getSimpleName() + " instead of SingleSelectionDefinitionDTO");
        }
        
        SingleSelectionDefinitionDTO singleSelectionDefinitionDto = (SingleSelectionDefinitionDTO)fieldDefinitionDto;
        
        FieldDefinition fieldDefinitionCreated = FieldDefinitionFactory.getBuilder(singleSelectionDefinitionDto.getType())
                                                                    .setName(singleSelectionDefinitionDto.getName())
                                                                    .addParameters(singleSelectionDefinitionDto.getSelections())
                                                                    .build();

        FieldDefinition fieldDefinitionFromDatabase = this.fieldDefinitionDao.save(fieldDefinitionCreated);

        return fieldDefinitionFromDatabase;
    }

    public FieldDefinitionDTO addSelections(String fieldDefinitionId, ArrayList<String> selections){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElseThrow();

        fieldDefinition.addMultipleEntry(selections);

        this.fieldDefinitionDao.save(fieldDefinition);

        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    } 

    public FieldDefinitionDTO removeSelections(String fieldDefinitionId, ArrayList<String> selections){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElseThrow();

        fieldDefinition.removeMultipleEntry(selections);

        this.fieldDefinitionDao.save(fieldDefinition);

        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    } 
    
}

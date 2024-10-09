package com.unifi.taskflow.businessLogic.services.fieldDefinitionServices;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.FieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.SingleSelectionDefinitionDTO;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.SingleSelectionDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders.SingleSelectionDefinitionBuilder;

@Service
public class SingleSelectionsDefinitionService extends FieldDefinitionService{

    @Transactional
    @Override
    public FieldDefinitionDTO updateFieldDefinition(FieldDefinitionDTO fieldDefinitionDto) {
        if (!(fieldDefinitionDto instanceof SingleSelectionDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinition is not SingleSelectionDefinitionDTO");
        }
        SingleSelectionDefinitionDTO singleSelectionDefinitionDto = (SingleSelectionDefinitionDTO) fieldDefinitionDto;

        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionDto.getId()).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }

        if (!(fieldDefinition instanceof SingleSelectionDefinition)){
            throw new IllegalArgumentException("FieldDefinition with id: " + fieldDefinition.getId() + " is not of type SingleSelectionDefinition");
        }

        SingleSelectionDefinition singleSelectionDefinition = (SingleSelectionDefinition)fieldDefinition;

        if (singleSelectionDefinitionDto.getName() != null){
            singleSelectionDefinition.setName(fieldDefinitionDto.getName());
        }
        if (singleSelectionDefinitionDto.getSelections() != null){
            singleSelectionDefinition.setPossibleSelections(singleSelectionDefinitionDto.getSelections());
        }

        this.fieldDefinitionDao.save(singleSelectionDefinition);

        return this.fieldDefinitionMapper.toDto(singleSelectionDefinition);
    }

    @Transactional
    @Override
    public FieldDefinition pushNewFieldDefinition(FieldDefinitionDTO fieldDefinitionDto) {
        if (fieldDefinitionDto == null){
            throw new IllegalArgumentException("FieldDefinitionDTO is null");
        }
        if (!(fieldDefinitionDto instanceof SingleSelectionDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinitionDto of class " + fieldDefinitionDto.getClass().getSimpleName() + " instead of SingleSelectionDefinitionDTO");
        }
        
        SingleSelectionDefinitionDTO singleSelectionDefinitionDto = (SingleSelectionDefinitionDTO)fieldDefinitionDto;

        if (singleSelectionDefinitionDto.getSelections() == null){
            throw new IllegalArgumentException("SingleSelection must have at least one selection");
        }

        if (singleSelectionDefinitionDto.getSelections().isEmpty()){
            throw new IllegalArgumentException("SingleSelection must have at least one selection");
        }

        FieldDefinition fieldDefinitionCreated = new SingleSelectionDefinitionBuilder()
                                                            .setSelections(singleSelectionDefinitionDto.getSelections())
                                                            .setName(singleSelectionDefinitionDto.getName())
                                                            .build();

        FieldDefinition fieldDefinitionFromDatabase = this.fieldDefinitionDao.save(fieldDefinitionCreated);

        return fieldDefinitionFromDatabase;
    }

    public FieldDefinitionDTO addSelections(String fieldDefinitionId, ArrayList<String> selections){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }

        ((SingleSelectionDefinition)fieldDefinition).addMultipleSelection(selections);

        this.fieldDefinitionDao.save(fieldDefinition);

        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    } 

    public FieldDefinitionDTO removeSelections(String fieldDefinitionId, ArrayList<String> selections){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }
        
        ((SingleSelectionDefinition)fieldDefinition).removeMultipleSelection(selections);

        this.fieldDefinitionDao.save(fieldDefinition);

        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    } 
    
}

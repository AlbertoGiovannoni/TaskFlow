package com.unifi.taskflow.businessLogic.services.fieldDefinitionServices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.FieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.SimpleFieldDefinitionDTO;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.SimpleFieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders.SimpleFieldDefinitionBuilder;

@Service
public class SimpleFieldDefinitionService extends FieldDefinitionService{
    @Transactional
    @Override
    public FieldDefinitionDTO updateFieldDefinition(FieldDefinitionDTO fieldDefinitionDto){
        if (!(fieldDefinitionDto instanceof SimpleFieldDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinitionDto is not of type SimpleFieldDefinitionDTO");
        }

        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionDto.getId()).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }
        
        if (!(fieldDefinition instanceof SimpleFieldDefinition)){
            throw new IllegalArgumentException("FieldDefinition is not of type SimpleFieldDefinition");
        }

        SimpleFieldDefinition simpleFieldDefinition = (SimpleFieldDefinition) fieldDefinition;

        simpleFieldDefinition.setName(fieldDefinitionDto.getName());

        this.fieldDefinitionDao.save(simpleFieldDefinition);

        return this.fieldDefinitionMapper.toDto(simpleFieldDefinition);
    }

    @Transactional
    @Override
    public FieldDefinition pushNewFieldDefinition(FieldDefinitionDTO fieldDefinitionDto){
        if (fieldDefinitionDto == null){
            throw new IllegalArgumentException("FieldDefinitionDTO is null");
        }
        if (!(fieldDefinitionDto instanceof SimpleFieldDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinitionDto of class " + fieldDefinitionDto.getClass().getSimpleName() + " instead of SingleSelectionDefinitionDTO");
        }
        
        SimpleFieldDefinitionDTO simpleFieldDefinitionDto = (SimpleFieldDefinitionDTO)fieldDefinitionDto;
        
        FieldDefinition fieldDefinitionCreated = new SimpleFieldDefinitionBuilder(simpleFieldDefinitionDto.getType())
                                                                    .setName(simpleFieldDefinitionDto.getName())
                                                                    .build();

        FieldDefinition fieldDefinitionFromDatabase = this.fieldDefinitionDao.save(fieldDefinitionCreated);

        return fieldDefinitionFromDatabase;
    };
}

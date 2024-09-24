package com.example.taskflow.service.FieldDefinitionServices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SimpleFieldDefinitionBuilder;

@Service
public class SimpleFieldDefinitionService extends FieldDefinitionService{
    @Transactional
    @Override
    public FieldDefinitionDTO updateFieldDefinition(FieldDefinitionDTO fieldDefinitionDto){
        if (!(fieldDefinitionDto instanceof SimpleFieldDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinitionDto is not of type SimpleFieldDefinitionDTO");
        }

        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionDto.getId()).orElseThrow();

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

package com.example.taskflow.service.FieldDefinitionServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.Mappers.FieldDefinitionMapper;

@Service
public class FieldDefinitionService {
    @Autowired
    FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    FieldDefinitionMapper fieldDefinitionMapper;

    public FieldDefinitionDTO createFieldDefinition(FieldDefinitionDTO fieldDefinitionDto){
        if (fieldDefinitionDto == null){
            throw new IllegalArgumentException("FieldDefinitionDTO is null");
        }
        if (!(fieldDefinitionDto instanceof SimpleFieldDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinitionDto of class " + fieldDefinitionDto.getClass().getSimpleName() + " instead of SingleSelectionDefinitionDTO");
        }
        
        SimpleFieldDefinitionDTO singleSelectionDefinitionDto = (SimpleFieldDefinitionDTO)fieldDefinitionDto;
        
        FieldDefinition fieldDefinitionCreated = FieldDefinitionFactory.getBuilder(singleSelectionDefinitionDto.getType())
                                                                    .setName(singleSelectionDefinitionDto.getName())
                                                                    .build();

        FieldDefinition fieldDefinitionFromDatabase = this.fieldDefinitionDao.save(fieldDefinitionCreated);

        return this.fieldDefinitionMapper.toDto(fieldDefinitionFromDatabase);
    };
}

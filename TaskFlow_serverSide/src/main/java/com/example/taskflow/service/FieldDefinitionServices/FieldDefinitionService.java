package com.example.taskflow.service.FieldDefinitionServices;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.SimpleFieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.FieldDefinitionMapper;

@Service
public class FieldDefinitionService {
    @Autowired
    FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    ProjectDAO projectDao;
    @Autowired
    FieldDAO fieldDao;
    @Autowired
    FieldDefinitionMapper fieldDefinitionMapper;
    @Autowired
    ActivityDAO activityDao;

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

    public FieldDefinitionDTO pushNewFieldDefinitionDTO(FieldDefinitionDTO fieldDefinitionDto){
        FieldDefinition fieldDefinition = this.pushNewFieldDefinition(fieldDefinitionDto);
        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    }

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

    public void deleteFieldDefinitionAndFieldsAndReferenceToFieldsInActivity(String fieldDefinitionId){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElseThrow();

        ArrayList<Field> fieldsToRemove = new ArrayList<>(this.fieldDao.findFieldByFieldDefinition(fieldDefinition));

        ArrayList<String> fieldToRemoveIds = this.getFieldIds(fieldsToRemove);

        this.fieldDao.deleteAll(fieldsToRemove);

        this.activityDao.removeFieldsFromActivities(fieldToRemoveIds);

        this.projectDao.removeFieldDefinitionFromProject(fieldDefinitionId);

        this.fieldDefinitionDao.deleteById(fieldDefinitionId);
    }

    public FieldDefinitionDTO renameFieldDefinition(String fieldDefinitionId, String name){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElseThrow();

        fieldDefinition.setName(name);

        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    }

    private ArrayList<String> getFieldIds(ArrayList<Field> fields){
        ArrayList<String> fieldIds = new ArrayList<>();

        for (Field field : fields){
            fieldIds.add(field.getId());
        }

        return fieldIds;
    }
}

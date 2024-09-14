package com.example.taskflow.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;

@Service
public class FieldDefinitionService {
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDao;

    @Autowired
    private FieldDAO fieldDao;

    public void delete(String id){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(id).orElseThrow();

        this.fieldDao.deleteFieldByFieldDefinition(fieldDefinition);

        this.fieldDefinitionDao.delete(fieldDefinition);
    }

    public FieldDefinition saveFieldDefinition(FieldDefinition fieldDefinition){
        return this.fieldDefinitionDao.save(fieldDefinition);
    }

    public FieldDefinition saveFieldDefinition(FieldType type, String name){
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(type)
                                            .setName(name)
                                            .build();

        this.fieldDefinitionDao.save(fieldDefinition);
        return fieldDefinition;
    }

    public FieldDefinition saveFieldDefinition(FieldType type, String name, ArrayList<?> parameters){
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(type)
                                            .setName(name)
                                            .addParameters(parameters)
                                            .build();
        this.fieldDefinitionDao.save(fieldDefinition);
        return fieldDefinition;
    }
}

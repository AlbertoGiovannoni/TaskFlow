package com.example.taskflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

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
}

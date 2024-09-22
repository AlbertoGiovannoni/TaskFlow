package com.example.taskflow.service.FieldService;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.FieldDefinitionMapper;
import com.example.taskflow.Mappers.FieldMapper;

@Service
public abstract class FieldService {

    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;

    @Autowired
    FieldMapper fieldMapper;

    @Autowired
    FieldDAO fieldDao;

    @Autowired
    FieldDefinitionMapper fieldDefinitionMapper;

    @Autowired
    ActivityDAO activityDao;

    abstract public Field pushNewField(FieldDTO fieldDto);

    public void deleteField(String fieldId){
        this.activityDao.removeFieldFromActivity(fieldId);
        this.fieldDao.deleteById(fieldId);
    }
}

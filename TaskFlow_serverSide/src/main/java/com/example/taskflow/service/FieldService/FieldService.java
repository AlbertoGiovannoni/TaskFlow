package com.example.taskflow.service.FieldService;

import java.util.ArrayList;

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

    public void deleteFieldAndActivityReference(String fieldId){
        this.activityDao.deleteFieldFromActivity(fieldId);
        this.fieldDao.deleteById(fieldId);
    }

    public ArrayList<String> deleteFieldsAndActivityReferences(ArrayList<Field> fields){
        ArrayList<String> fieldIds = this.getFieldIds(fields);

        this.activityDao.deleteFieldsFromActivities(fieldIds);

        this.fieldDao.deleteAll(fields);

        return fieldIds;
    }

    private ArrayList<String> getFieldIds(ArrayList<Field> fields){
        ArrayList<String> fieldIds = new ArrayList<>();

        for (Field field : fields){
            fieldIds.add(field.getId());
        }

        return fieldIds;
    }
}

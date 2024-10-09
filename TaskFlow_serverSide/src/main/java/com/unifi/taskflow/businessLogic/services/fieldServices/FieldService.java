package com.unifi.taskflow.businessLogic.services.fieldServices;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.mappers.FieldDefinitionMapper;
import com.unifi.taskflow.businessLogic.mappers.FieldMapper;
import com.unifi.taskflow.daos.ActivityDAO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.domainModel.fields.Field;

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

    abstract public Field updateField(FieldDTO fieldDto);

    public void deleteFieldAndActivityReference(String fieldId){
        this.activityDao.removeFieldFromActivity(fieldId);
        this.fieldDao.deleteById(fieldId);
    }

    public ArrayList<String> deleteFieldsAndActivityReferences(ArrayList<Field> fields){
        ArrayList<String> fieldIds = this.getFieldIds(fields);

        this.activityDao.removeFieldsFromActivities(fieldIds);

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

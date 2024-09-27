package com.example.taskflow.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.EntityFactory;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.ActivityMapper;
import com.example.taskflow.Mappers.FieldMapper;
import com.example.taskflow.service.FieldService.FieldServiceManager;

@Service
public class ActivityService {

    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    FieldMapper fieldMapper;
    @Autowired
    FieldServiceManager fieldServiceManager;
    @Autowired
    FieldDAO fieldDao;
    @Autowired
    ActivityDAO activityDao;
    @Autowired
    UserDAO userDao;
    @Autowired
    FieldDefinitionDAO fieldDefinitionDao;

    public Activity pushNewActivity(ActivityDTO activityDTO) {
        if (activityDTO.getUuid() == null) { // TODO perche non ho uuid mentre in user si
            activityDTO.setUuid(UUID.randomUUID().toString());
        }
        ArrayList<FieldDTO> fieldsDto = new ArrayList<FieldDTO>();
        
        if (activityDTO.getFields().size() != 0) {
            fieldsDto = activityDTO.getFields();
        }
        ArrayList<Field> fields = new ArrayList<Field>();

        for (FieldDTO movingFieldDto : fieldsDto) {
            fields.add(
                    this.fieldServiceManager
                            .getFieldService(movingFieldDto)
                            .pushNewField(movingFieldDto));
        }

        Activity activity = EntityFactory.getActivity();
        activity.setName(activityDTO.getName());
        activity.setFields(fields);

        activity = this.activityDao.save(activity);

        return activity;
    }

    public void deleteActivityAndFields(String activityId) {
        Activity activity = this.activityDao.findById(activityId).orElseThrow();

        this.fieldDao.deleteAll(activity.getFields());

        // TODO: eliminare ref in project

        this.activityDao.delete(activity);
    }

    public Activity renameActivity(String activityId, String newName) {
        Activity activity = this.activityDao.findById(activityId).orElseThrow();

        activity.setName(newName);

        return this.activityDao.save(activity);
    }

    public ActivityDTO getActivityById(String activityId) {
        Activity activity = this.activityDao.findById(activityId).orElseThrow();
        return this.activityMapper.toDto(activity);
    }

    public ActivityDTO addFieldToActivity(String activityId, FieldDTO fieldDto) {
        Activity activity = this.activityDao.findById(activityId).orElseThrow();

        Field field = this.fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);

        activity.addField(field);
        activityDao.save(activity);

        return this.activityMapper.toDto(activity);
    }

    public FieldDTO updateField(FieldDTO fieldDto) {
        Field field = this.fieldServiceManager.getFieldService(fieldDto).updateField(fieldDto);

        return this.fieldMapper.toDto(field);
    }

    public ActivityDTO removeField(String fieldId) {
        Field field = this.fieldDao.findById(fieldId).orElseThrow();
        // this.fieldDao.deleteFiledByActivity(fieldId); //TODO da implementare in
        // fieldDao
        return null;
    }

}

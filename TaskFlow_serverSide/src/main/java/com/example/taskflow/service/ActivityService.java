package com.example.taskflow.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.Activity;
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

        ArrayList<FieldDTO> fieldsDto = activityDTO.getFields();
        ArrayList<Field> fields = new ArrayList<Field>();

        for (FieldDTO movingFieldDto : fieldsDto) {
            fields.add(
                    this.fieldServiceManager
                            .getFieldService(movingFieldDto)
                                            .pushNewField(movingFieldDto));
        }

        Activity activity = this.activityMapper.toEntity(activityDTO);
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

    public Activity renameActivity(String activityId, String newName){
        Activity activity = this.activityDao.findById(activityId).orElseThrow();

        activity.setName(newName);

        return this.activityDao.save(activity);
    }

    public ActivityDTO getActivityById(String activityId){
        Activity activity = this.activityDao.findById(activityId).orElseThrow();
        return this.activityMapper.toDto(activity);
    }
}

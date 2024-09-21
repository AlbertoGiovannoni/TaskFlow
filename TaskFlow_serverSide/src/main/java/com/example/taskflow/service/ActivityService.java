package com.example.taskflow.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ActivityDAO;
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
    ActivityDAO activityDao;
    @Autowired
    UserDAO userDao;
    @Autowired
    FieldDefinitionDAO fieldDefinitionDao;

    public ActivityDTO createActivity(ActivityDTO activityDTO) {

        ArrayList<FieldDTO> fieldsDto = activityDTO.getFields();
        ArrayList<Field> fields = new ArrayList<Field>();

        for (FieldDTO movingFieldDto : fieldsDto) {
            fields.add(
                    this.fieldServiceManager
                            .getFieldService(movingFieldDto).getField(
                                    this.fieldServiceManager
                                            .getFieldService(movingFieldDto)
                                            .createField(movingFieldDto)));
        }

        Activity activity = this.activityMapper.toEntity(activityDTO);
        activity.setFields(fields);

        activity = this.activityDao.save(activity);

        return this.activityMapper.toDto(this.activityDao.save(activity));
    }

    public void deleteActivity(ActivityDTO activityDTO) {

        Activity activity = this.activityDao.findById(activityDTO.getId()).orElseThrow();
        for (Field field : activity.getFields()) {
            // fieldService.deleteField(Field); //PENDING: implement deleteField in
            // fieldService
        }
        this.activityDao.delete(activity);
    }
}

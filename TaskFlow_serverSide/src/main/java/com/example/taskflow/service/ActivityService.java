package com.example.taskflow.service;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.ActivityDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.EntityFactory;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.ActivityMapper;
import com.example.taskflow.Mappers.FieldMapper;
import com.example.taskflow.service.FieldService.FieldServiceManager;

import jakarta.validation.constraints.Null;

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
    @Autowired
    ProjectDAO projectDao;

    public Activity pushNewActivity(ActivityDTO activityDTO) {

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

        Project project = this.projectDao.findProjectByActivity(activityId);
        project.deleteActivity(activity);

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
        Activity activity = this.activityDao.findById(activityId).orElse(null);

        if (activity == null) {
            throw new IllegalArgumentException("Wrong activity id");
        }

        Field field = this.fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto);

        activity.addField(field);
        activityDao.save(activity);

        return this.activityMapper.toDto(activity);
    }

    public FieldDTO updateField(FieldDTO fieldDto) {
        Field field = this.fieldServiceManager.getFieldService(fieldDto).updateField(fieldDto);

        return this.fieldMapper.toDto(field);
    }

    public ActivityDTO removeField(String activityId, String fieldId) {
        Activity activity = this.activityDao.findById(activityId).orElseThrow();
        Field field = this.fieldDao.findById(fieldId).orElseThrow();

        activity.removeField(field);

        this.fieldDao.delete(field);
        activityDao.save(activity);
        return this.activityMapper.toDto(activity);
    }

}

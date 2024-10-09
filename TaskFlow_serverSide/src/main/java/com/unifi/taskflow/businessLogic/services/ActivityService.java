package com.unifi.taskflow.businessLogic.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.ActivityDTO;
import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.mappers.ActivityMapper;
import com.unifi.taskflow.businessLogic.mappers.FieldMapper;
import com.unifi.taskflow.businessLogic.services.fieldServices.FieldServiceManager;
import com.unifi.taskflow.daos.ActivityDAO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.ProjectDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.Activity;
import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.Project;
import com.unifi.taskflow.domainModel.fields.Field;

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
        Activity activity = this.activityDao.findById(activityId).orElse(null);

        if (activity == null){
            throw new IllegalArgumentException("Activity not found");
        }

        this.fieldDao.deleteAll(activity.getFields());

        Project project = this.projectDao.findProjectByActivity(activityId);
        project.deleteActivity(activity);

        this.projectDao.save(project);

        this.activityDao.delete(activity);
    }

    public Activity renameActivity(String activityId, String newName) {
        Activity activity = this.activityDao.findById(activityId).orElse(null);

        if (activity == null) {
            throw new IllegalArgumentException("Activity not found");
        }

        activity.setName(newName);

        return this.activityDao.save(activity);
    }

    public ActivityDTO getActivityById(String activityId) {
        Activity activity = this.activityDao.findById(activityId).orElse(null);

        if (activity == null){
            throw new IllegalArgumentException("Activity not found");
        }
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

    public ActivityDTO addFieldsToActivity(String activityId, ArrayList<FieldDTO> fieldDtos){
        Activity activity = this.activityDao.findById(activityId).orElse(null);

        if (activity == null){
            throw new IllegalArgumentException("Activity not found");
        }

        ArrayList<Field> fieldsPushed = new ArrayList<>();

        for (FieldDTO fieldDto : fieldDtos){
            fieldsPushed.add(this.fieldServiceManager.getFieldService(fieldDto).pushNewField(fieldDto));
        }

        activity.addFields(fieldsPushed);
        activity = this.activityDao.save(activity);

        return this.activityMapper.toDto(activity);
    }

    public FieldDTO updateField(FieldDTO fieldDto) {
        Field field = this.fieldServiceManager.getFieldService(fieldDto).updateField(fieldDto);

        return this.fieldMapper.toDto(field);
    }

    public ArrayList<FieldDTO> updateFields(ArrayList<FieldDTO> fieldDtos) {
        ArrayList<FieldDTO> fieldDtosPushed = new ArrayList<>();

        for (FieldDTO fieldDto : fieldDtos){
            fieldDtosPushed.add(this.fieldMapper.toDto(this.fieldServiceManager.getFieldService(fieldDto).updateField(fieldDto)));
        }

        return fieldDtosPushed;
    }

    public ActivityDTO removeField(String activityId, String fieldId) {
        Activity activity = this.activityDao.findById(activityId).orElse(null);

        if (activity == null){
            throw new IllegalArgumentException("Activity not found");
        }

        Field field = this.fieldDao.findById(fieldId).orElse(null);

        if (field == null){
            throw new IllegalArgumentException("Field not found");
        }

        activity.removeField(field);

        this.fieldDao.delete(field);
        activityDao.save(activity);
        return this.activityMapper.toDto(activity);
    }

}

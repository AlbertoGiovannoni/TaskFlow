package com.example.taskflow.service.FieldDefinitionServices;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.FieldDefinitionMapper;


public abstract class FieldDefinitionService {
    
    @Autowired
    FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    ProjectDAO projectDao;
    @Autowired
    FieldDAO fieldDao;
    @Autowired
    FieldDefinitionMapper fieldDefinitionMapper;
    @Autowired
    ActivityDAO activityDao;

    public abstract FieldDefinitionDTO updateFieldDefinition(FieldDefinitionDTO fieldDefinitionDto);

    public abstract FieldDefinition pushNewFieldDefinition(FieldDefinitionDTO fieldDefinitionDto);

    public FieldDefinitionDTO pushNewFieldDefinitionDTO(FieldDefinitionDTO fieldDefinitionDto){
        FieldDefinition fieldDefinition = this.pushNewFieldDefinition(fieldDefinitionDto);
        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    }

    public void deleteFieldDefinitionAndFieldsAndReferenceToFieldsInActivity(String fieldDefinitionId){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElseThrow();

        ArrayList<Field> fieldsToRemove = new ArrayList<>(this.fieldDao.findFieldByFieldDefinition(fieldDefinition));

        ArrayList<String> fieldToRemoveIds = this.getFieldIds(fieldsToRemove);

        //this.activityDao.removeFieldsFromActivities(fieldToRemoveIds);
        this.removeFieldReferenceFromActivities(fieldToRemoveIds);

        //this.projectDao.removeFieldDefinitionFromProject(fieldDefinitionId);
        this.removeFieldDefinitionReferenceFromProject(fieldDefinition);

        this.fieldDao.deleteAll(fieldsToRemove);

        this.fieldDefinitionDao.deleteById(fieldDefinitionId);
    }

    public FieldDefinitionDTO renameFieldDefinition(String fieldDefinitionId, String name){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElseThrow();

        fieldDefinition.setName(name);

        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    }

    private ArrayList<String> getFieldIds(ArrayList<Field> fields){
        ArrayList<String> fieldIds = new ArrayList<>();

        for (Field field : fields){
            fieldIds.add(field.getId());
        }

        return fieldIds;
    }

    private void removeFieldReferenceFromActivities(ArrayList<String> fieldIds){
        ArrayList<Activity> activitiesFound = this.activityDao.getActivitiesByFieldIds(fieldIds);
        ArrayList<Field> fields = new ArrayList<>(this.fieldDao.findAllById(fieldIds));

        if (activitiesFound != null){
            for (Activity activity : activitiesFound){
                for (Field field : fields){
                    activity.removeField(field);
                }
                this.activityDao.save(activity);
            }
        }
    }

    private void removeFieldDefinitionReferenceFromProject(FieldDefinition fieldDefinition){
        Project project = this.projectDao.findProjectByFieldDefinition(fieldDefinition.getId());

        if (project != null){
            project.removeFieldDefinition(fieldDefinition);
            this.projectDao.save(project);
        }
    }
}

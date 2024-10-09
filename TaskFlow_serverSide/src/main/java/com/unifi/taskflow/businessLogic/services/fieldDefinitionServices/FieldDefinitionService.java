package com.unifi.taskflow.businessLogic.services.fieldDefinitionServices;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.FieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.mappers.FieldDefinitionMapper;
import com.unifi.taskflow.daos.ActivityDAO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.ProjectDAO;
import com.unifi.taskflow.domainModel.Activity;
import com.unifi.taskflow.domainModel.Project;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Field;


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

    public FieldDefinitionDTO pushNewFieldDefinitionDTO(FieldDefinitionDTO fieldDefinitionDto, String projectId){
        FieldDefinition fieldDefinition = this.pushNewFieldDefinition(fieldDefinitionDto);

        Project project = this.projectDao.findById(projectId).orElse(null);

        if (project == null){
            throw new IllegalArgumentException("Project not found");
        }

        project.addFieldDefinition(fieldDefinition);
        projectDao.save(project);

        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    }

    public void deleteFieldDefinitionAndFieldsAndReferenceToFieldsInActivity(String fieldDefinitionId){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }

        ArrayList<Field> fieldsToRemove = new ArrayList<>(this.fieldDao.findFieldByFieldDefinition(fieldDefinition));

        ArrayList<String> fieldToRemoveIds = this.getFieldIds(fieldsToRemove);

        this.removeFieldReferenceFromActivities(fieldToRemoveIds);

        this.removeFieldDefinitionReferenceFromProject(fieldDefinition);

        this.fieldDao.deleteAll(fieldsToRemove);

        this.fieldDefinitionDao.deleteById(fieldDefinitionId);
    }

    public FieldDefinitionDTO renameFieldDefinition(String fieldDefinitionId, String name){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }
        
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

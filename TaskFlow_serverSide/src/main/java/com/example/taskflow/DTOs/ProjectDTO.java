package com.example.taskflow.DTOs;

import java.util.ArrayList;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

import jakarta.validation.constraints.NotNull;

public class ProjectDTO {

    private String id;
    @NotNull
    private String name;
    private ArrayList<FieldDefinitionDTO> fieldsTemplate;
    private ArrayList<ActivityDTO> activities;

    public ProjectDTO(String id, @NotNull String name, ArrayList<FieldDefinitionDTO> fieldsTemplate,
            ArrayList<ActivityDTO> activities) {
        this.id = id;
        this.name = name;
        this.fieldsTemplate = fieldsTemplate;
        this.activities = activities;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<FieldDefinitionDTO> getFieldsTemplate() {
        return fieldsTemplate;
    }
    public void setFieldsTemplate(ArrayList<FieldDefinitionDTO> fieldsTemplate) {
        this.fieldsTemplate = fieldsTemplate;
    }
    public ArrayList<ActivityDTO> getActivities() {
        return activities;
    }
    public void setActivities(ArrayList<ActivityDTO> activities) {
        this.activities = activities;
    }
}

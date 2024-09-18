package com.example.taskflow.DTOs;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

import jakarta.validation.constraints.NotNull;

public class ProjectDTO {

    private String id;
    @NotNull
    private String name;
    private ArrayList<FieldDefinition> fieldsTemplate;
    private ArrayList<Activity> activities;

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
    public ArrayList<FieldDefinition> getFieldsTemplate() {
        return fieldsTemplate;
    }
    public void setFieldsTemplate(ArrayList<FieldDefinition> fieldsTemplate) {
        this.fieldsTemplate = fieldsTemplate;
    }
    public ArrayList<Activity> getActivities() {
        return activities;
    }
    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}

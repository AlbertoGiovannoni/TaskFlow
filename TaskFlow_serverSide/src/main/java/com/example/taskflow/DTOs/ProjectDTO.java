package com.example.taskflow.DTOs;

import java.util.ArrayList;

import jakarta.validation.constraints.NotNull;

public class ProjectDTO {

    private String id;
    @NotNull
    private String name;
    private ArrayList<String> fieldsTemplateId;
    private ArrayList<String> activitiesId;

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
    public ArrayList<String> getFieldsTemplateId() {
        return fieldsTemplateId;
    }
    public void setFieldsTemplateId(ArrayList<String> fieldsTemplateId) {
        this.fieldsTemplateId = fieldsTemplateId;
    }
    public ArrayList<String> getActivitiesId() {
        return activitiesId;
    }
    public void setActivitiesId(ArrayList<String> activitiesId) {
        this.activitiesId = activitiesId;
    }
}

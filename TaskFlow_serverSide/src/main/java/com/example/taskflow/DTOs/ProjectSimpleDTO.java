package com.example.taskflow.DTOs;

import java.util.ArrayList;

public class ProjectSimpleDTO {
    private String id;
    private String name;
    private ArrayList<String> fieldTemplateIds;
    private ArrayList<String> activityIds;
    
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
    public ArrayList<String> getFieldTemplateIds() {
        return fieldTemplateIds;
    }
    public void setFieldTemplateIds(ArrayList<String> fieldTemplateIds) {
        this.fieldTemplateIds = fieldTemplateIds;
    }
    public ArrayList<String> getActivityIds() {
        return activityIds;
    }
    public void setActivityIds(ArrayList<String> activityIds) {
        this.activityIds = activityIds;
    }

    
}

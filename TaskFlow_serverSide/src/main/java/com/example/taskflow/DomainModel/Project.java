package com.example.taskflow.DomainModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;


@Document
public class Project {
    
    @Id
    private String id;
    private String name;
    private ArrayList<FieldDefinition> availableFields;
    private ArrayList<Activity> activities;

    
    // costruttore di default
    public Project(){
    }
    
    public Project(String name) {
        this.name = name;
    }
    
    public ArrayList<Activity> getAllActivities() {
        return this.activities;
    }
    
    public void addActivity(Activity newAct) {
        activities.add(newAct);
    }
    
    public void deleteActivity(String id) {
        activities.removeIf(activity -> activity.getId().equals(id));
    }
    
    public void addFieldDefinition(FieldType type, String name) {
        FieldDefinition newFieldDef = FieldDefinitionBuilder.buildField(type, name);
        availableFields.add(newFieldDef);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ArrayList<FieldDefinition> getAvailableFields() {
        return availableFields;
    }
    
    public void setAvailableFields(ArrayList<FieldDefinition> availableFields) {
        this.availableFields = availableFields;
    }
    
    public ArrayList<Activity> getActivities() {
        return activities;
    }
    
    public void setActivities(ArrayList<Activity> activities) {
        this.activities = activities;
    }
}

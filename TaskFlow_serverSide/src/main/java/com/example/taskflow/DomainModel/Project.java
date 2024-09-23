package com.example.taskflow.DomainModel;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import java.util.ArrayList;


@Document
public class Project extends BaseEntity{
    private String name;

    @DBRef
    private ArrayList<FieldDefinition> fieldsTemplate;

    @DBRef
    private ArrayList<Activity> activities;
    
    // costruttore di default
    public Project(){
        super();
    }

    public Project(String uuid, String name){
        super(uuid);
        this.name = name;
        this.activities = new ArrayList<Activity>();
        this.fieldsTemplate = new ArrayList<FieldDefinition>();
    }
    
    public Project(String uuid, String name, ArrayList<FieldDefinition> fieldsTemplate, ArrayList<Activity> activities) {
        super(uuid);
        this.name = name;
        this.fieldsTemplate = fieldsTemplate;
        this.activities = activities;
    }

    public void addFieldDefinition(FieldDefinition fieldDefinition) {
        fieldsTemplate.add(fieldDefinition);
    }
    
    public ArrayList<Activity> getAllActivities() {
        return this.activities;
    }
    
    public void addActivity(Activity newAct) {
        activities.add(newAct);
    }

    public boolean deleteActivity(Activity actToRemove) {
        return activities.remove(actToRemove);
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

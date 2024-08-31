package com.example.taskflow.DomainModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import java.util.ArrayList;
import java.util.UUID;


@Document
public class Project implements UuidInterface{
    
    @Id
    private String id;
    private String name;
    private UUID uuid;

    @DBRef
    private ArrayList<FieldDefinition> fieldsTemplate;

    @DBRef
    private ArrayList<Activity> activities;
    
    // costruttore di default
    public Project(){
    }

    public Project(String name){
        this.name = name;
        this.activities = new ArrayList<Activity>();
        this.fieldsTemplate = new ArrayList<FieldDefinition>();
    }
    
    public Project(String name, ArrayList<FieldDefinition> fieldsTemplate, ArrayList<Activity> activities) {
        this.name = name;
        this.fieldsTemplate = fieldsTemplate;
        this.activities = activities;
        this.uuid = UUID.randomUUID();
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
    
    // getter e setter
    
    public String getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
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

    @Override
    public boolean equals(Object obj) {
        boolean value = false;

        if (obj != null && obj instanceof Project){
            if (obj instanceof Project){
                value = (this.uuid.equals(((Project)obj).getUuid()));  
            }
        }

        return value;
    }
}

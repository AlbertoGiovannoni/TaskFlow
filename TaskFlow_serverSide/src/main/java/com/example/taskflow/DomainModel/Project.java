package com.example.taskflow.DomainModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import java.util.ArrayList;
import java.util.UUID;


@Document
public class Project {
    
    @Id
    private String id;
    private String name;
    private UUID uuid;
    private ArrayList<FieldDefinition> fieldsTemplate;

    @DBRef
    private ArrayList<Activity> activities;
    
    // costruttore di default
    public Project(){
    }
    
    public Project(String name, ArrayList<FieldDefinition> fieldsTemplate, ArrayList<Activity> activities) {
        this.name = name;
        this.fieldsTemplate = fieldsTemplate;
        this.activities = activities;
        this.uuid = UUID.randomUUID();
    }

    //TODO perchè aggiungo un field definition da dentro project? non dovrei passargli un oggetto fieldDefinition già costruito?
    public void addFieldDefinition(FieldType type, String name) {
        FieldDefinition newFieldDef = FieldDefinitionBuilder.buildField(type, name);
        fieldsTemplate.add(newFieldDef);
    }

    //TODO controlla se questi metodi devono stare qui
    public void deleteActivity(String id) {
        activities.removeIf(activity -> activity.getId().equals(id));
    }
    
    public ArrayList<Activity> getAllActivities() {
        return this.activities;
    }
    
    public void addActivity(Activity newAct) {
        activities.add(newAct);
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
}

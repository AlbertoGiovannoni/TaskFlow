package com.example.taskflow.DomainModel.FieldDefinitionPackage;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.taskflow.DomainModel.UuidInterface;

import java.util.ArrayList;

@Document("fieldDefinition")
public abstract class FieldDefinition implements UuidInterface{

    @Id
    String id;
    String name;
    FieldType type;
    String uuid;

    // Costruttore di default
    public FieldDefinition() {}
    
    public FieldDefinition(String name, FieldType type) {
        this.type = type;
        this.name = name;
    }

    public abstract void addSingleEntry(Object obj);

    public abstract void addMultipleEntry(ArrayList<?> obj);

    public abstract void removeEntry(Object obj);

    public abstract void removeMultipleEntry(ArrayList<?> objs);

    public abstract void reset();

    public abstract ArrayList<?> getAllEntries();

    public abstract Object getSingleEntry();

    public abstract boolean validateValue(Object obj);
    
    @Override
    public boolean equals(Object obj) {
        boolean value = false;
        
        if (obj != null){
            if (obj instanceof FieldDefinition){
                value = this.uuid.equals(((FieldDefinition)obj).getUuid());
            }
        }
        
        return value;
    }
    
    // getter e setter
    public String getName() {
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public FieldType getType(){
        return this.type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}


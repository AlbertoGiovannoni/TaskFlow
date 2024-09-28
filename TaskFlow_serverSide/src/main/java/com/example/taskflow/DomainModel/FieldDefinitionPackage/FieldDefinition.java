package com.example.taskflow.DomainModel.FieldDefinitionPackage;

import org.springframework.data.mongodb.core.mapping.Document;

import com.example.taskflow.DomainModel.BaseEntity;

@Document("fieldDefinition")
public abstract class FieldDefinition extends BaseEntity{
    String name;
    FieldType type;

    // Costruttore di default
    public FieldDefinition() {
        super();
    }

    public FieldDefinition(String uuid) {
        super(uuid);
    }
    
    public FieldDefinition(String uuid, String name, FieldType type) {
        this.type = type;
        this.name = name;
    }

    public abstract void reset();

    public abstract boolean validateValue(Object obj);
    
    // getter e setter
    public String getName() {
        return name;
    }
    
    public void setName(String name){
        this.name = name;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public FieldType getType(){
        return this.type;
    }
}


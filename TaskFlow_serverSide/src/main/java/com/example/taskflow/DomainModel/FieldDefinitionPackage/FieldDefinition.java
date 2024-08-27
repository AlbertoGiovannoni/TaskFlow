package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UuidInterface;

import java.lang.UnsupportedOperationException;

@Document
public abstract class FieldDefinition implements UuidInterface{

    @Id
    String id;
    String name;
    FieldType type;
    UUID uuid;

    // Costruttore di default
    public FieldDefinition() {}
    
    public FieldDefinition(String name, FieldType type) {
        this.type = type;
        this.name = name;
        this.uuid = UUID.randomUUID();
    }
    
    public abstract void validateValue();
    
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

    public void addUser(User user){
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + "not implements method" + this.getClass().getEnclosingMethod().toString());
    }

    // getter e setter
    public String getName() {
        return name;
    }
    
    public String getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public FieldType getType(){
        return this.type;
    }
}


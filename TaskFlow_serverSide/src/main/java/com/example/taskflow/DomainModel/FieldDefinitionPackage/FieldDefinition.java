package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class FieldDefinition {

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
}


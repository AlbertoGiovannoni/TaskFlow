package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public abstract class FieldDefinition {

    @Id
    String id;
    String name;
    FieldType type;

    public FieldDefinition(String name, FieldType type) {
        this.type = type;
        this.name = name;
    }

    public abstract void validateValue();

    // getter e setter

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}


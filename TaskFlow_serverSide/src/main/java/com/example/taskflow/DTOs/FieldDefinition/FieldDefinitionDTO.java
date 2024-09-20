package com.example.taskflow.DTOs.FieldDefinition;

import jakarta.validation.constraints.NotBlank;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldDefinitionDTO {
    @NotBlank(message = "Nome non può essere vuoto")
    String name;
    @NotBlank(message = "Type non può essere vuoto")
    FieldType type;
    String id;
    String uuid;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

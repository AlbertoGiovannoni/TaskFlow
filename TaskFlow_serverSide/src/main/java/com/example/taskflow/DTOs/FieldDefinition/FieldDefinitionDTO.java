package com.example.taskflow.DTOs.FieldDefinition;

import jakarta.validation.constraints.NotNull;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldDefinitionDTO {
    @NotNull
    private String name;
    @NotNull
    private FieldType type;
    private String id;


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
}

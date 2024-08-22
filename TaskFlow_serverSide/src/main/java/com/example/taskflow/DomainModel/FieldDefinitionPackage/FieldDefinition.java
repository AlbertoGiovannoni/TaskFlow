package com.example.taskflow.DomainModel.FieldDefinitionPackage;

public abstract class FieldDefinition {

    private String name;

    public FieldDefinition(String name) {
        this.name = name;
    }

    public abstract void validateValue();
}


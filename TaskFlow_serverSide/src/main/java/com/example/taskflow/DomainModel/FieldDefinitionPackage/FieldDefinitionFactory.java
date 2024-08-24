package com.example.taskflow.DomainModel.FieldDefinitionPackage;


public abstract class FieldDefinitionFactory<T extends FieldDefinition, B extends FieldDefinitionFactory<T, B>> {
    protected String name;
    protected FieldType type;

    public B addCommon(String name, FieldType type) {
        this.name = name;
        this.type = type;
        return self();
    }

    protected abstract B self();

    public abstract T build();
}


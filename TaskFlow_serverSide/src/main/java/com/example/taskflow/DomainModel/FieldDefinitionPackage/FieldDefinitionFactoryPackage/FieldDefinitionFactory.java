package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldDefinitionFactory<T extends FieldDefinition, B extends FieldDefinitionFactory<T, B>> {
    String name;
    FieldType type;

    public B addCommonAttributes(String name, FieldType type) {
        this.name = name;
        this.type = type;
        return self();
    }

    protected abstract B self();

    public abstract T build();
}


package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldDefinitionBuilder<T extends FieldDefinition, SPECIFIC_TYPE, B extends FieldDefinitionBuilder<T, SPECIFIC_TYPE, B>> {
    String name;
    FieldType type;

    FieldDefinitionBuilder(FieldType type){
        this.type = type;
    }

    public B addCommonAttributes(String name) {
        this.name = name;
        return self();
    }

    public B addSpecificField(SPECIFIC_TYPE value){
        throw new IllegalAccessError(this.getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    abstract B self();

    public abstract T build();
}


package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldDefinitionBuilder {
    String name;
    FieldType type;

    FieldDefinitionBuilder(FieldType type){
        this.type = type;
    }

    public FieldDefinitionBuilder addCommonAttributes(String name) {
        this.name = name;
        return self();
    }

    public FieldDefinitionBuilder addSpecificField(ArrayList<User> values){
        throw new IllegalAccessError(this.getClass().getSimpleName() + " not implement method addSpecificField()");
    }

    abstract FieldDefinitionBuilder self();

    public abstract FieldDefinition build();
}


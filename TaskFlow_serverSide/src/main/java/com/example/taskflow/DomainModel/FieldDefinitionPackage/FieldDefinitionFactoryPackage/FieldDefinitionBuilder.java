package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldDefinitionBuilder {
    String name;
    FieldType type;

    FieldDefinitionBuilder(FieldType type){
        this.type = type;
    }

    public FieldDefinitionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public abstract FieldDefinitionBuilder reset();
    
    public abstract FieldDefinition build();
}


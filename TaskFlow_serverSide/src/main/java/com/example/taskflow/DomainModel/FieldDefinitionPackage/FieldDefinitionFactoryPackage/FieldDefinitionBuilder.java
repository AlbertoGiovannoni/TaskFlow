package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

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

    public abstract FieldDefinitionBuilder addParameters(ArrayList<?> values);

    public abstract FieldDefinitionBuilder addParameter(Object value);

    public abstract void reset();
    
    public abstract FieldDefinition build();
}


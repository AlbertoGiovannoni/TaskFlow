package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public abstract class FieldBuilder {
    FieldDefinition fieldDefinition;

    FieldBuilder(FieldDefinition fieldDefinition){
        this.fieldDefinition = fieldDefinition;
    }

    public abstract Field build();
}

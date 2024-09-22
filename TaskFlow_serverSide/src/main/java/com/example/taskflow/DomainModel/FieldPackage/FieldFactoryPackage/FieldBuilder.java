package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.util.ArrayList;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldBuilder {
    FieldDefinition fieldDefinition;

    FieldBuilder(FieldDefinition fieldDefinition){
        this.fieldDefinition = fieldDefinition;
    }

    public abstract Field build();
}

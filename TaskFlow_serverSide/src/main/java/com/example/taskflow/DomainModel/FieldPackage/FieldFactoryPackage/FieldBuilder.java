package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.util.ArrayList;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public abstract class FieldBuilder {
    FieldType type;
    FieldDefinition fieldDefinition;

    FieldBuilder(FieldType type){
        this.type = type;
    }

    public FieldBuilder addFieldDefinition(FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
        return this;
    }

    public abstract FieldBuilder addParameters(ArrayList<?> values);

    public abstract FieldBuilder addParameter(Object value);

    public abstract Field build();
}

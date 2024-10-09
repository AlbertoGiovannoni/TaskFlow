package com.unifi.taskflow.domainModel.fields.fieldBuilders;

import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Field;

public abstract class FieldBuilder {
    FieldDefinition fieldDefinition;

    FieldBuilder(FieldDefinition fieldDefinition){
        this.fieldDefinition = fieldDefinition;
    }

    public abstract Field build();
}

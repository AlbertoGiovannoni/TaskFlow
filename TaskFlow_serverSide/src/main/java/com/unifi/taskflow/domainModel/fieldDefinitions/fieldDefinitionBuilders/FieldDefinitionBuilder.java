package com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders;

import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;

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


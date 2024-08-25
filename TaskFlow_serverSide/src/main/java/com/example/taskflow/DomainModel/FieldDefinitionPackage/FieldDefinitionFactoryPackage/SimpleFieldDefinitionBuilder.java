package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;

public class SimpleFieldDefinitionBuilder extends FieldDefinitionBuilder{

    SimpleFieldDefinitionBuilder(FieldType type) {
        super(type);
    }

    @Override
    protected SimpleFieldDefinitionBuilder self() {
        return this;
    }

    @Override
    public SimpleFieldDefinition build() {
        return new SimpleFieldDefinition(this.name, this.type);
    }
}

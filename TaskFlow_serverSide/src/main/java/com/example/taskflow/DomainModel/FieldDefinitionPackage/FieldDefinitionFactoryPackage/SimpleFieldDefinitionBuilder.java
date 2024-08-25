package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;

public class SimpleFieldDefinitionBuilder extends FieldDefinitionBuilder<SimpleFieldDefinition, SimpleFieldDefinitionBuilder> {

    @Override
    protected SimpleFieldDefinitionBuilder self() {
        return this;
    }

    @Override
    public SimpleFieldDefinition build() {
        return new SimpleFieldDefinition(name, type);
    }
}

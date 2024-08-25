package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;

public class SimpleFieldDefinitionFactory extends FieldDefinitionFactory<SimpleFieldDefinition, SimpleFieldDefinitionFactory> {

    @Override
    protected SimpleFieldDefinitionFactory self() {
        return this;
    }

    @Override
    public SimpleFieldDefinition build() {
        return new SimpleFieldDefinition(name, type);
    }
}

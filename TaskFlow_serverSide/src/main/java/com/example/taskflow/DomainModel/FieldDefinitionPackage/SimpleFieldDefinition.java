package com.example.taskflow.DomainModel.FieldDefinitionPackage;

public class SimpleFieldDefinition extends FieldDefinition {
    public SimpleFieldDefinition() {}

    public SimpleFieldDefinition(String name, FieldType type) {
        super(name, type);
    }

    @Override
    public void validateValue() {
        //TODO
    }
}
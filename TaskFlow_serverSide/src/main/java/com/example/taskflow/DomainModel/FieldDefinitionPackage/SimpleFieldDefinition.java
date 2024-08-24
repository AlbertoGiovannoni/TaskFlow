package com.example.taskflow.DomainModel.FieldDefinitionPackage;


class SimpleFieldDefinition extends FieldDefinition {
    public SimpleFieldDefinition(String name, FieldType type) {
        super(name, type);
    }

    @Override
    public void validateValue() {
        //TODO
    }
}
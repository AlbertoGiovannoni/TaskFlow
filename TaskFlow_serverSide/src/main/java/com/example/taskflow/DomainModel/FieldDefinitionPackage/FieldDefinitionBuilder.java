package com.example.taskflow.DomainModel.FieldDefinitionPackage;


public class FieldDefinitionBuilder {
    public static FieldDefinition buildField(FieldType type, String name) {
        switch (type) {
            case ASSIGNEE:
                return new AssigneeDefinition(name, type);
            case SINGLE_SELECTION:
                return new SingleSelectionDefinition(name, type);
            default:
                return new SimpleFieldDefinition(name, type);
        }
    }
}

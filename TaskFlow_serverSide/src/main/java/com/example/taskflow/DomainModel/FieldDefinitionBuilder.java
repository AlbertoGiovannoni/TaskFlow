package com.example.taskflow.DomainModel;

public class FieldDefinitionBuilder {
    public static FieldDefinition buildField(FieldType type, String name) {
        switch (type) {
            case ASSIGNEE:
                return new AssigneeDefinition(name);
            case SINGLE_SELECTION:
                return new SingleSelectionDefinition(name);
            default:
                return new SimpleFieldDefinition(name);
        }
    }
}

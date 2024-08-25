package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class FieldDefinitionFactory {
    public static FieldDefinitionBuilder getBuilder(FieldType type){
        switch (type) {
            case ASSIGNEE:
                return new AssigneeDefinitionBuilder(type);
            case SINGLE_SELECTION:
                return new SingleSelectionDefinitionBuilder(type);
            case DATE:
                return new SimpleFieldDefinitionBuilder(type);
            case NUMBER:
                return new SimpleFieldDefinitionBuilder(type);
            case DOCUMENT:
                return new SimpleFieldDefinitionBuilder(type);
            case TEXT:
                return new SimpleFieldDefinitionBuilder(type);
            default:
                throw new IllegalArgumentException("Unsopported type: " + type);
        }
    }
}

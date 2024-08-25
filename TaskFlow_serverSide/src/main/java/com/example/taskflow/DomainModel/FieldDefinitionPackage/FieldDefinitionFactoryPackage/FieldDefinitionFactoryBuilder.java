package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class FieldDefinitionFactoryBuilder {
    public static FieldDefinitionBuilder<?, ?> getBuilder(FieldType type){
        switch (type) {
            case ASSIGNEE:
                return new AssigneeDefinitionBuilder();
            case SINGLE_SELECTION:
                return new SingleSelectionDefinitionBuilder();
            case DATE:
                return new SimpleFieldDefinitionBuilder();
            case NUMBER:
                return new SimpleFieldDefinitionBuilder();
            case DOCUMENT:
                return new SimpleFieldDefinitionBuilder();
            case TEXT:
                return new SimpleFieldDefinitionBuilder();
            default:
                throw new IllegalArgumentException("Unsopported type: " + type);
        }
    }
}

package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class FieldDefinitionFactoryBuilder {
    public static FieldDefinitionFactory<?, ?> getBuilder(FieldType type){
        switch (type) {
            case ASSIGNEE:
                return new AssegneeDefinitionFactory();
            case SINGLE_SELECTION:
                return new SingleSelectionDefinitionFactory();
            case DATE:
                return new SimpleFieldDefinitionFactory();
            case NUMBER:
                return new SimpleFieldDefinitionFactory();
            case DOCUMENT:
                return new SimpleFieldDefinitionFactory();
            case TEXT:
                return new SimpleFieldDefinitionFactory();
            default:
                throw new IllegalArgumentException("Unsopported type: " + type);
        }
    }
}

package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class FieldFactory {
    public static FieldBuilder getBuilder(FieldType type){
        switch (type) {
            case ASSIGNEE:
                return new AssigneeBuilder(type);
            case SINGLE_SELECTION:
                return new SingleSelectionBuilder(type);
            case DATE:
                return new DateBuilder(type);
            case NUMBER:
                return new NumberBuilder(type);
            case DOCUMENT:
                return new DocumentBuilder(type);
            case TEXT:
                return new TextBuilder(type);
            default:
                throw new IllegalArgumentException("Unsopported type: " + type);
        }
    }
}
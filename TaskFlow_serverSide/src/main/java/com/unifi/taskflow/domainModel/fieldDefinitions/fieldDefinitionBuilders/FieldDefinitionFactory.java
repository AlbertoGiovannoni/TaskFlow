package com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders;

import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;

public class FieldDefinitionFactory {
    public static FieldDefinitionBuilder getBuilder(FieldType type){
        switch (type) {
            case ASSIGNEE:
                return new AssigneeDefinitionBuilder();
            case SINGLE_SELECTION:
                return new SingleSelectionDefinitionBuilder();
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

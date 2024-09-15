package com.example.taskflow.service.FieldDefinitionServices;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class FieldDefinitionServiceManager {
    public static FieldDefinitionService getFieldDefinitionManager(FieldType type){
        switch (type) {
            case ASSIGNEE:
                return new AssigneeDefinitionService();
            case SINGLE_SELECTION:
                return new SingleSelectionDefinitionService();
            default:
                return new FieldDefinitionService();
        }
    }
}

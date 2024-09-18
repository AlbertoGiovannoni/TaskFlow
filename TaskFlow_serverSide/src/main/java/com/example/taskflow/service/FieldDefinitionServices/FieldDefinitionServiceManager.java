package com.example.taskflow.service.FieldDefinitionServices;

import org.springframework.stereotype.Service;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;

@Service
public class FieldDefinitionServiceManager {
    public FieldDefinitionService getFieldDefinitionService(FieldDefinitionDTO fieldDefinitionDto){
        switch (fieldDefinitionDto.getType()) {
            case ASSIGNEE:
                return new AssigneeDefinitionService();
            case SINGLE_SELECTION:
                return new SingleSelectionsDefinitionService();
            case NUMBER:
                return new FieldDefinitionService();
            case TEXT:
                return new FieldDefinitionService();
            case DATE:
                return new FieldDefinitionService();
            case DOCUMENT:
                //TODO
                throw new IllegalArgumentException("Document need implementation");
            default:
                throw new IllegalArgumentException(fieldDefinitionDto.getType().toString() + " not recognized");
        }
    }
}

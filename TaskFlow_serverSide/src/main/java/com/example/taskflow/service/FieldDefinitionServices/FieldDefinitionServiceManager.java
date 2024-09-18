package com.example.taskflow.service.FieldDefinitionServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;

@Service
public class FieldDefinitionServiceManager {
    @Autowired
    private FieldDefinitionService fieldDefinitionService;

    @Autowired
    private AssigneeDefinitionService assigneeDefinitionService;

    @Autowired
    private SingleSelectionsDefinitionService singleSelectionsDefinitionService;

    public FieldDefinitionService getFieldDefinitionService(FieldDefinitionDTO fieldDefinitionDto){
        switch (fieldDefinitionDto.getType()) {
            case ASSIGNEE:
                return this.assigneeDefinitionService;
            case SINGLE_SELECTION:
                return this.singleSelectionsDefinitionService;
            case NUMBER:
            case TEXT:
            case DATE:
                return this.fieldDefinitionService;
            case DOCUMENT:
                //TODO
                throw new IllegalArgumentException("Document need implementation");
            default:
                throw new IllegalArgumentException(fieldDefinitionDto.getType().toString() + " not recognized");
        }
    }
}

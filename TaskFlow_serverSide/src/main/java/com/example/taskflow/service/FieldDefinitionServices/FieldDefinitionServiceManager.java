package com.example.taskflow.service.FieldDefinitionServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

@Service
public class FieldDefinitionServiceManager {
    @Autowired
    private SimpleFieldDefinitionService simpleFieldDefinitionService;
    @Autowired
    private AssigneeDefinitionService assigneeDefinitionService;
    @Autowired
    private SingleSelectionsDefinitionService singleSelectionsDefinitionService;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDAO;

    public FieldDefinitionService getFieldDefinitionService(FieldDefinitionDTO fieldDefinitionDto){
        return this.switchFieldDefinition(fieldDefinitionDto.getType());
    }

    public FieldDefinitionService getFieldDefinitionService(String fieldDefinitionId){
        FieldDefinition fieldDefinition = this.fieldDefinitionDAO.findById(fieldDefinitionId).orElseThrow();
        
        return this.switchFieldDefinition(fieldDefinition.getType());
    }

    private FieldDefinitionService switchFieldDefinition(FieldType type){
        switch (type) {
            case ASSIGNEE:
                return this.assigneeDefinitionService;
            case SINGLE_SELECTION:
                return this.singleSelectionsDefinitionService;
            case NUMBER:
            case TEXT:
            case DATE:
            case DOCUMENT:
                return this.simpleFieldDefinitionService;
            default:
                throw new IllegalArgumentException(type + " not recognized");
        }
    }
}

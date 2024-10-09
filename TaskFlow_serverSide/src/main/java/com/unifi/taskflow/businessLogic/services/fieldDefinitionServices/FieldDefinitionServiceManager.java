package com.unifi.taskflow.businessLogic.services.fieldDefinitionServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.FieldDefinitionDTO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;

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
        FieldDefinition fieldDefinition = this.fieldDefinitionDAO.findById(fieldDefinitionId).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }
        
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

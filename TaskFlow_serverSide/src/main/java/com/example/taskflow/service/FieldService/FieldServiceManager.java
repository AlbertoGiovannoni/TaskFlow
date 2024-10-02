package com.example.taskflow.service.FieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

@Service
public class FieldServiceManager {

    @Autowired
    AssigneeService assigneeService;
    @Autowired
    SingleSelectionService singleSelectionService;
    @Autowired
    DateService dateService;
    @Autowired
    DocumentService documentService;
    @Autowired
    NumberService numberService;
    @Autowired
    TextService textService;

    public FieldService getFieldService(FieldDTO fieldDto) {
        return this.getFieldService(fieldDto.getType());
    }

    public FieldService getFieldService(FieldType type) {

        switch (type) {
            case ASSIGNEE:
                return this.assigneeService;
            case SINGLE_SELECTION:
                return this.singleSelectionService;
            case NUMBER:
                return this.numberService;
            case TEXT:
                return this.textService;
            case DATE:
                return this.dateService;
            case DOCUMENT:
                return this.documentService;
            default:
                throw new IllegalArgumentException(type.toString() + " not recognized");
        }
    }

}

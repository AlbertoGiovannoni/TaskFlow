package com.example.taskflow.service.FieldService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DTOs.Field.FieldDTO;

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

        switch (fieldDto.getType()) {
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
                // TODO
                throw new IllegalArgumentException("Document need implementation");
            default:
                throw new IllegalArgumentException(fieldDto.getType().toString() + " not recognized");
        }

    }

}

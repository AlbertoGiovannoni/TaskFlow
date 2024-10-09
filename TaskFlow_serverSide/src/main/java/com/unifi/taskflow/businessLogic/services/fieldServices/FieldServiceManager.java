package com.unifi.taskflow.businessLogic.services.fieldServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;
import com.unifi.taskflow.domainModel.fields.Field;

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
    @Autowired
    FieldDAO fieldDao;

    public FieldService getFieldService(FieldDTO fieldDto) {
        return this.getFieldService(fieldDto.getType());
    }

    public FieldService getFieldService(String fieldId) {
        if (fieldId == null){
            throw new IllegalArgumentException("FieldId must not be null");
        }
        if (fieldId.isBlank()){
            throw new IllegalArgumentException("FieldId must not be blank");
        }

        Field field = this.fieldDao.findById(fieldId).orElse(null);

        if (field == null){
            throw new IllegalArgumentException("Field not found in database");
        }

        return this.getFieldService(field.getType());
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

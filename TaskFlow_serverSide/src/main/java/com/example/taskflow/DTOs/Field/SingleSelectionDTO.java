package com.example.taskflow.DTOs.Field;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class SingleSelectionDTO extends FieldDTO{
    private String value;

    public SingleSelectionDTO() {
        this.setType(FieldType.SINGLE_SELECTION);  // Set the type explicitly in constructor
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}

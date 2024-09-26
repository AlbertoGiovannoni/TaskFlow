package com.example.taskflow.DTOs.Field;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class TextDTO extends FieldDTO{
    private String value;

    public TextDTO() {
        this.setType(FieldType.TEXT);  // Set the type explicitly in constructor
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

package com.example.taskflow.DTOs;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class FieldDTO {

    private String id;
    @NotNull
    private FieldType type;
    @NotNull
    private ArrayList<String> value;
    @NotNull
    private String fieldDefinitionId;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public FieldType getType() {
        return type;
    }
    public void setType(FieldType type) {
        this.type = type;
    }
    public ArrayList<String> getValue() {
        return value;
    }
    public void setValue(ArrayList<String> value) {
        this.value = value;
    }
    public String getFieldDefinitionId() {
        return fieldDefinitionId;
    }
    public void setFieldDefinitionId(String fieldDefinitionId) {
        this.fieldDefinitionId = fieldDefinitionId;
    }
    
}

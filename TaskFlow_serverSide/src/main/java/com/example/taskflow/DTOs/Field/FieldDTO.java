package com.example.taskflow.DTOs.Field;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

import jakarta.validation.constraints.NotBlank;

public abstract class FieldDTO {

    String id;
    String uuid;
    FieldType type;
    ArrayList<String> valuesDto;
    
    @NotBlank(message = "FieldDefinition non può essere vuoto")
    String fieldDefinitionId;

    public String getId() {
        return id;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFieldDefinitionId() {
        return fieldDefinitionId;
    }

    public void setFieldDefinitionId(String fieldDefinitionId) {
        this.fieldDefinitionId = fieldDefinitionId;
    }

    public ArrayList<String> getValuesDto() {
        return valuesDto;
    }

    public void setValuesDto(ArrayList<String> values) {
        this.valuesDto = values;
    }
    
    
}

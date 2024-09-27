package com.example.taskflow.DTOs.Field;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.validation.constraints.NotBlank;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)

@JsonSubTypes({
    @JsonSubTypes.Type(value = NumberDTO.class, name = "NUMBER"),
    @JsonSubTypes.Type(value = TextDTO.class, name = "TEXT"),
    @JsonSubTypes.Type(value = AssigneeDTO.class, name = "ASSIGNEE"),
    @JsonSubTypes.Type(value = DateDTO.class, name = "DATE"),
    @JsonSubTypes.Type(value = SingleSelectionDTO.class, name = "SINGLE_SELECTION"),
})

public abstract class FieldDTO {

    String id;
    String uuid;
    FieldType type;
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
}

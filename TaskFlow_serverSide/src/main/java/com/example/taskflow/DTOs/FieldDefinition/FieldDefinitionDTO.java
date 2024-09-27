package com.example.taskflow.DTOs.FieldDefinition;

import jakarta.validation.constraints.NotBlank;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AssigneeDefinitionDTO.class, name = "ASSIGNEE"),
    @JsonSubTypes.Type(value = SingleSelectionDefinitionDTO.class, name = "SINGLE_SELECTION"),
    @JsonSubTypes.Type(value = SimpleFieldDefinitionDTO.class, name = "SIMPLE")
})

public abstract class FieldDefinitionDTO {
    @NotBlank(message = "Nome non può essere vuoto")
    String name;
    FieldType type;
    String id;
    String uuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

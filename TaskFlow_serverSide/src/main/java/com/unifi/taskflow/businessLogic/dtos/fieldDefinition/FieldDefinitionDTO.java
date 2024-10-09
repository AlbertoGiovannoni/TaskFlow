package com.unifi.taskflow.businessLogic.dtos.fieldDefinition;

import jakarta.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;
import com.fasterxml.jackson.annotation.JsonSubTypes;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AssigneeDefinitionDTO.class, name = "ASSIGNEE"),
    @JsonSubTypes.Type(value = SingleSelectionDefinitionDTO.class, name = "SINGLE_SELECTION"),
    @JsonSubTypes.Type(value = SimpleFieldDefinitionDTO.class, name = "DATE"),
    @JsonSubTypes.Type(value = SimpleFieldDefinitionDTO.class, name = "NUMBER"),
    @JsonSubTypes.Type(value = SimpleFieldDefinitionDTO.class, name = "TEXT"),
    @JsonSubTypes.Type(value = SimpleFieldDefinitionDTO.class, name = "DOCUMENT")
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

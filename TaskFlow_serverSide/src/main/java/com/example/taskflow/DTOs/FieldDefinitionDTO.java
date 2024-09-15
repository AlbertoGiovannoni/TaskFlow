package com.example.taskflow.DTOs;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class FieldDefinitionDTO {
    @NotNull
    private String name;
    @NotNull
    private FieldType type;
    private String id;
    private ArrayList<String> parameters;


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

    public ArrayList<String> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }
}

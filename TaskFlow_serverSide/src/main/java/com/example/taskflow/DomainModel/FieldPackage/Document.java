package com.example.taskflow.DomainModel.FieldPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;


public class Document extends Field{

    private String name;

    public Document() {
    }

    public Document(FieldDefinition fieldDefinition, String name) {
        super(fieldDefinition);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

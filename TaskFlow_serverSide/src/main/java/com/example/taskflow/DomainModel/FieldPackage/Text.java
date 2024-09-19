package com.example.taskflow.DomainModel.FieldPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Text extends Field{

    private String text;

    // costruttore di default
    public Text(){
    }

    public Text(FieldDefinition fieldDefinition, String value) {
        super(fieldDefinition);

        this.text = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

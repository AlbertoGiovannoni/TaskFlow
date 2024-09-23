package com.example.taskflow.DomainModel.FieldPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Text extends Field{

    private String value;

    // costruttore di default
    public Text(){
        super();
    }

    public Text(String uuid){
        super(uuid);
    }

    public Text(String uuid, FieldDefinition fieldDefinition, String value) {
        super(uuid, fieldDefinition);

        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String text) {
        this.value = text;
    }
}

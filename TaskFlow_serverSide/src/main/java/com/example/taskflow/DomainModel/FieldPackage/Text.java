package com.example.taskflow.DomainModel.FieldPackage;

import java.util.UUID;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Text extends Field{

    private String value;
    private UUID uuid;

    // costruttore di default
    public Text(){
    }

    public Text(FieldDefinition fieldDefinition, String value) {
        super(fieldDefinition);

        this.value = value;
        this.uuid = UUID.randomUUID();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UUID getUuid() {
        return uuid;
    }
}

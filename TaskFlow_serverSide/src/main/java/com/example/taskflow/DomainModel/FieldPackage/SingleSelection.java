package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class SingleSelection extends Field{

    private ArrayList<String> value;
    private UUID uuid;

    // costruttore di default
    public SingleSelection(){
    }

    public SingleSelection(FieldDefinition fieldDefinition, ArrayList<String> value) {
        super(fieldDefinition);

        this.value = value;
        this.uuid = UUID.randomUUID();
    }

    public ArrayList<String> getValue() {
        return value;
    }

    public void setValue(ArrayList<String> value) {
        this.value = value;
    }

    public UUID getUuid() {
        return uuid;
    }
}

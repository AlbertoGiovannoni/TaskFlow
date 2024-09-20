package com.example.taskflow.DomainModel.FieldPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;


public class SingleSelection extends Field {

    private String value;

    // costruttore di default
    public SingleSelection() {
    }

    public SingleSelection(FieldDefinition fieldDefinition, String value) {
        super(fieldDefinition);

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String selection) {
        if (((AssigneeDefinition)this.fieldDefinition).validateValue(selection)){
            this.value = selection;
        }
    }
}

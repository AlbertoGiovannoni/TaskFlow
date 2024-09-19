package com.example.taskflow.DomainModel.FieldPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;


public class SingleSelection extends Field {

    private String selection;

    // costruttore di default
    public SingleSelection() {
    }

    public SingleSelection(FieldDefinition fieldDefinition, String value) {
        super(fieldDefinition);

        this.selection = value;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        if (((AssigneeDefinition)this.fieldDefinition).validateValue(selection)){
            this.selection = selection;
        }
    }
}

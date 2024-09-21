package com.example.taskflow.DomainModel.FieldPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;


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

    public void setValue(String value) {
        if (((SingleSelectionDefinition)this.fieldDefinition).validateValue(value)){
            this.value = value;
        }
        else{
            throw new IllegalArgumentException(value + " not allowed: fieldDefinition = [" + this.fieldDefinition.getAllEntries() + "]");
        }
    }
}

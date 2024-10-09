package com.unifi.taskflow.domainModel.fields;

import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.SingleSelectionDefinition;


public class SingleSelection extends Field {

    private String value;

    // costruttore di default
    public SingleSelection() {
        super();
    }

    public SingleSelection(String uuid) {
        super(uuid);
    }

    public SingleSelection(String uuid, FieldDefinition fieldDefinition, String value) {
        super(uuid, fieldDefinition);

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
            throw new IllegalArgumentException(value + " not allowed: fieldDefinition = [" + ((SingleSelectionDefinition) this.fieldDefinition).getPossibleSelections() + "]");
        }
    }
}

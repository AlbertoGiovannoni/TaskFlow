package com.unifi.taskflow.businessLogic.dtos.fieldDefinition;

import java.util.ArrayList;

public class SingleSelectionDefinitionDTO extends FieldDefinitionDTO{
    ArrayList<String> selections;

    public ArrayList<String> getSelections() {
        return selections;
    }

    public void setSelections(ArrayList<String> selections) {
        this.selections = selections;
    }
}

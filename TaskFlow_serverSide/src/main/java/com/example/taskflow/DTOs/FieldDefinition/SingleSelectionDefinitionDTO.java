package com.example.taskflow.DTOs.FieldDefinition;

import java.util.ArrayList;

public class SingleSelectionDefinitionDTO extends FieldDefinitionDTO{
    ArrayList<String> possibleSelections;

    public ArrayList<String> getSelections() {
        return possibleSelections;
    }

    public void setSelections(ArrayList<String> selections) {
        this.possibleSelections = selections;
    }
}

package com.example.taskflow.DTOs.FieldDefinition;

import java.util.ArrayList;

public class SingleSelectionDefinitionDTO extends FieldDefinitionDTO{
    ArrayList<String> possibleSelections;

    public ArrayList<String> getPossibleSelections() {
        return possibleSelections;
    }

    public void setPossibleSelections(ArrayList<String> selections) {
        this.possibleSelections = selections;
    }
}

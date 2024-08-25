package com.example.taskflow.DomainModel.FieldDefinitionPackage;

import java.util.ArrayList;

public class SingleSelectionDefinitionFactory extends FieldDefinitionFactory<SingleSelectionDefinition, SingleSelectionDefinitionFactory> {
    private ArrayList<String> possibleSelections;

    public SingleSelectionDefinitionFactory addSpecificField(ArrayList<String> possibleSelections) {
        this.possibleSelections = possibleSelections;
        return this;
    }

    @Override
    protected SingleSelectionDefinitionFactory self() {
        return this;
    }

    @Override
    public SingleSelectionDefinition build() {
        return new SingleSelectionDefinition(name, FieldType.SINGLE_SELECTION, possibleSelections);
    }
}


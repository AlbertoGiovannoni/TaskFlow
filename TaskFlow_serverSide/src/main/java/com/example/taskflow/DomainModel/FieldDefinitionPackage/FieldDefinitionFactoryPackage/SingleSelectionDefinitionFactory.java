package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;

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


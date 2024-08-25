package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;

public class SingleSelectionDefinitionBuilder extends FieldDefinitionBuilder<SingleSelectionDefinition, SingleSelectionDefinitionBuilder> implements SpecificBuilder<SingleSelectionDefinitionBuilder, ArrayList<String>>{
    private ArrayList<String> possibleSelections;

    @Override
    public SingleSelectionDefinitionBuilder addSpecificField(ArrayList<String> possibleSelections) {
        this.possibleSelections = possibleSelections;
        return this;
    }

    @Override
    protected SingleSelectionDefinitionBuilder self() {
        return this;
    }

    @Override
    public SingleSelectionDefinition build() {
        return new SingleSelectionDefinition(name, FieldType.SINGLE_SELECTION, possibleSelections);
    }
}


package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;


public class SingleSelectionDefinitionBuilder extends FieldDefinitionBuilder{
    
    private ArrayList<String> possibleSelections;
    
    public SingleSelectionDefinitionBuilder() {
        super(FieldType.SINGLE_SELECTION);
    }

    @Override
    public SingleSelectionDefinition build() {
        return new SingleSelectionDefinition(this.name, this.type, possibleSelections);
    }

    @Override
    public SingleSelectionDefinitionBuilder reset() {
        this.possibleSelections.clear();
        return this;
    }

    public SingleSelectionDefinitionBuilder setSelections(ArrayList<String> selections){
        this.possibleSelections = selections;
        return this;
    }
}


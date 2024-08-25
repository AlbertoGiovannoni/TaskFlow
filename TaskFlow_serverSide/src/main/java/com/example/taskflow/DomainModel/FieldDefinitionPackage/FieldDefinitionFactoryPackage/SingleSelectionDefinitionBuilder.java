package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;

public class SingleSelectionDefinitionBuilder extends FieldDefinitionBuilder<SingleSelectionDefinition, ArrayList<String>, SingleSelectionDefinitionBuilder>{
    
    private ArrayList<String> possibleSelections;
    
    SingleSelectionDefinitionBuilder(FieldType type) {
        super(type);
    }

    @Override
    public SingleSelectionDefinitionBuilder addSpecificField(ArrayList<String> possibleSelections) {
        this.possibleSelections = possibleSelections;
        return this.self();
    }

    @Override
    protected SingleSelectionDefinitionBuilder self() {
        return this;
    }

    @Override
    public SingleSelectionDefinition build() {
        return new SingleSelectionDefinition(this.name, this.type, possibleSelections);
    }
}


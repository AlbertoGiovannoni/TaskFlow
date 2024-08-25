package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;

public class SingleSelectionDefinitionBuilder extends FieldDefinitionBuilder<SingleSelectionDefinition, SingleSelectionDefinitionBuilder>{
    
    private ArrayList<String> possibleSelections;
    
    SingleSelectionDefinitionBuilder(FieldType type) {
        super(type);
    }

    @Override
    public SingleSelectionDefinitionBuilder addSpecificField(ArrayList<Object> possibleSelections) {
        ArrayList<String> entrySelections = new ArrayList<>();

        for (Object obj : possibleSelections){
            entrySelections.add((String)obj);
        }

        this.possibleSelections = entrySelections;
        return this;
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


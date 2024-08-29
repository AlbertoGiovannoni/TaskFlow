package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;

public class SingleSelectionDefinitionBuilder extends FieldDefinitionBuilder{
    
    private ArrayList<String> possibleSelections;
    
    SingleSelectionDefinitionBuilder(FieldType type) {
        super(type);
        this.possibleSelections = new ArrayList<>();
    }

    @Override
    public SingleSelectionDefinition build() {
        return new SingleSelectionDefinition(this.name, this.type, possibleSelections);
    }


    @Override
    public FieldDefinitionBuilder addParameters(ArrayList<?> values) {
        for (Object value : values){
            this.addParameter(value);
        }
        return this;
    }


    @Override
    public FieldDefinitionBuilder addParameter(Object value) {
        if (value != null){
            if (value instanceof String){
                this.possibleSelections.add((String)value);
            }
        }
        return this;
    }


    @Override
    public void reset() {
        this.possibleSelections.clear();
    }
}


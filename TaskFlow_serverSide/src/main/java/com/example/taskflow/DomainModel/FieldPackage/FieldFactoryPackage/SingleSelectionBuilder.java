package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.SingleSelection;

public class SingleSelectionBuilder extends FieldBuilder{
    
    private ArrayList<String> selections;
    
    SingleSelectionBuilder(FieldType type) {
        super(type);
        this.selections = new ArrayList<>();
    }

    @Override
    public FieldBuilder addParameters(ArrayList<?> values){
        for (Object value : values){
            this.addParameter(value);
        }
        return this;
    }

    @Override
    public FieldBuilder addParameter(Object value){
        if (value != null){
            if (value instanceof String){
                this.selections.add((String)value);
            }
        }
        return this;
    }

    @Override
    protected SingleSelectionBuilder self() {
        return this;
    }

    @Override
    public SingleSelection build() {
        return new SingleSelection(this.fieldDefinition, selections);
    }
}


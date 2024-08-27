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
    public SingleSelectionBuilder setSelection(ArrayList<String> values){
        this.selections = values;
        return this.self();
    }

    @Override
    protected SingleSelectionBuilder self() {
        return this;
    }

    @Override
    public SingleSelection build() {
        return new SingleSelection( selections, this.fieldDefinition);
    }
}


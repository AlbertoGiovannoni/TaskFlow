package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.SingleSelection;

public class SingleSelectionBuilder extends FieldBuilder{
    
    private String selection;
    
    SingleSelectionBuilder(FieldType type) {
        super(type);
        this.selection = "";
    }

    @Override
    public FieldBuilder addParameters(ArrayList<?> values){
        return this.addParameter(values.get(0));
    }

    @Override
    public FieldBuilder addParameter(Object value){
        if (value != null){
            if (value instanceof String){
                this.selection = (String)value;
            }
        }
        return this;
    }

    @Override
    public SingleSelection build() {
        return new SingleSelection(this.fieldDefinition, selection);
    }
}


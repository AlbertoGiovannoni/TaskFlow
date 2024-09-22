package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.SingleSelection;

public class SingleSelectionBuilder extends FieldBuilder {

    private String selection;

    public SingleSelectionBuilder(FieldDefinition fieldDefinition) {
        super(fieldDefinition);
        this.selection = "";
    }

    public FieldBuilder addSelection(String value) {
        if (value != null) {
            this.selection = value;
        } else {
            throw new IllegalArgumentException("selection is null:" + value);
        }
        return this;
    }

    @Override
    public SingleSelection build() {
        if (this.selection == null) {
            throw new IllegalAccessError("selection is null");
        }
        return new SingleSelection(this.fieldDefinition, selection);
    }
}

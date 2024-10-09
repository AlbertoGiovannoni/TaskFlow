package com.unifi.taskflow.domainModel.fields.fieldBuilders;

import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.SingleSelection;

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

        SingleSelection singleSelection = EntityFactory.getSingleSelection();

        singleSelection.setFieldDefinition(this.fieldDefinition);
        singleSelection.setValue(this.selection);

        return singleSelection;
    }
}

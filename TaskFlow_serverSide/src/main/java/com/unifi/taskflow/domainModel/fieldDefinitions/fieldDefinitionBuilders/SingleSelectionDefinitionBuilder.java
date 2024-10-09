package com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders;

import java.util.ArrayList;

import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;
import com.unifi.taskflow.domainModel.fieldDefinitions.SingleSelectionDefinition;


public class SingleSelectionDefinitionBuilder extends FieldDefinitionBuilder{
    
    private ArrayList<String> possibleSelections;
    
    public SingleSelectionDefinitionBuilder() {
        super(FieldType.SINGLE_SELECTION);
    }

    @Override
    public SingleSelectionDefinition build() {
        SingleSelectionDefinition singleSelectionDefinition = EntityFactory.getSingleSelectionDefinition();

        singleSelectionDefinition.setName(this.name);
        singleSelectionDefinition.setType(this.type);
        singleSelectionDefinition.setPossibleSelections(possibleSelections);

        return singleSelectionDefinition;
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


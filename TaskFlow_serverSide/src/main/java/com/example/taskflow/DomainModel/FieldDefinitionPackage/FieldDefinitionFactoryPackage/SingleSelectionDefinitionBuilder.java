package com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;

public class SingleSelectionDefinitionBuilder extends FieldDefinitionBuilder{
    
    private ArrayList<String> possibleSelections;
    
    SingleSelectionDefinitionBuilder(FieldType type) {
        super(type);
        this.possibleSelections = new ArrayList<>();
    }

    
    @Override
    public SingleSelectionDefinitionBuilder setSpecificField(ArrayList<Object> possibleSelections) {
        this.checkMyPossibleSelections();
        this.possibleSelections.clear();

        for (Object obj : possibleSelections){
            this.possibleSelections.add((String)obj);
        }

        return this.self();
    }

    @Override
    public FieldDefinitionBuilder addSpecificField(Object value) {
        this.checkMyPossibleSelections();

        this.possibleSelections.add((String)value);

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

    private void checkMyPossibleSelections(){
        if (this.possibleSelections == null){
            throw new IllegalArgumentException(this.getClass().getSimpleName() + " possibleSelections is null");
        }
    }
}


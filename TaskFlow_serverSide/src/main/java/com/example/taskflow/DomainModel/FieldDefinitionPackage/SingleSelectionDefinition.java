package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;

class SingleSelectionDefinition extends FieldDefinition {

    private ArrayList<String> possibleSelections;        //TODO implementa le possibile selezioni

    public SingleSelectionDefinition(String nome, FieldType type, ArrayList<String> possibleSelections) {
        super(nome, type);
        this.possibleSelections = possibleSelections;
    }
    
    public SingleSelectionDefinition(String nome, FieldType type) {
        super(nome, type);
    }

    @Override
    public void validateValue() {
        //TODO
    }
}

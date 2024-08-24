package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;

class SingleSelectionDefinition extends FieldDefinition {

    private ArrayList<String> possibleSelection;        //TODO implementa le possibile selezioni

    public SingleSelectionDefinition(String nome) {
        super(nome);
    }

    @Override
    public void validateValue() {
        //TODO
    }
}

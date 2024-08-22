package com.example.taskflow.DomainModel.FieldDefinitionPackage;

import java.util.Set;

class SingleSelectionDefinition extends FieldDefinition {

    private Set<String> possibleSelection;        //TODO implementa le possibile selezioni

    public SingleSelectionDefinition(String nome) {
        super(nome);
    }

    @Override
    public void validateValue() {
        //TODO
    }
}

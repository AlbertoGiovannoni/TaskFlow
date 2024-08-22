package com.example.taskflow.DomainModel;

import java.util.ArrayList;
import java.util.Set;

public abstract class FieldDefinition {

    private String name;

    public FieldDefinition(String name) {
        this.name = name;
    }

    public abstract void validateValue();
}

class AssigneeDefinition extends FieldDefinition {

    private ArrayList<User> possibleAssegneeUsers;      //TODO implementa lista di user

    public AssigneeDefinition(String nome) {
        super(nome);
    }

    @Override
    public void validateValue() {
        //TODO
    }
}

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

class SimpleFieldDefinition extends FieldDefinition {
    public SimpleFieldDefinition(String nome) {
        super(nome);
    }

    @Override
    public void validateValue() {
        //TODO
    }
}
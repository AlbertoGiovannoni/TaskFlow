package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;

import com.example.taskflow.DomainModel.User;

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

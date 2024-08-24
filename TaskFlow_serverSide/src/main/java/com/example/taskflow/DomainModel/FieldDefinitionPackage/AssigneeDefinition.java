package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.taskflow.DomainModel.User;

class AssigneeDefinition extends FieldDefinition {

    @DBRef
    private ArrayList<User> possibleAssegneeUsers;      //TODO implementa lista di user

    public AssigneeDefinition(String nome, FieldType type) {
        super(nome, type);
    }

    @Override
    public void validateValue() {
        //TODO
    }

    public void addUser(User user){
        this.possibleAssegneeUsers.add(user);
    }

    public void addUsers(ArrayList<User> users){
        this.possibleAssegneeUsers.addAll(users);
    }
}

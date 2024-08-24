package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.taskflow.DomainModel.User;

public class AssigneeDefinition extends FieldDefinition {

    /*
        FIXME: forse qua dobbiamo avere una reference ad organization?
               Quando vado a costruire un nuovo FieldDefinition almeno 
               non avrei bisogno di creare tutti gli oggetti
               di tipo User?
               Inoltre se aggiungo users duplicati questa cosa andrebbe gestita
               qua se metto una reference diretta agli users mentre se metto
               reference a organization posso gestirla in quella classe
               (che secondo me ha senso perché l'organizzazione ha la
               responsabilità di tenere gli utenti come un 'set').
               No scherzo forse ha senso metterci direttamente gli users,
               però allora l'utente ha possibilità di modificare la lista 
               degli utenti ammessi a questo campo?
    */ 
    @DBRef
    private ArrayList<User> possibleAssigneeUsers;

    public AssigneeDefinition(String name, FieldType type) {
        super(name, type);
        this.possibleAssigneeUsers = new ArrayList<>();
    }

    public AssigneeDefinition(String nome, FieldType type, ArrayList<User> users) {
        super(nome, type);
        this.possibleAssigneeUsers = users;
    }

    @Override
    public void validateValue() {
        //TODO
    }

    //FIXME: se si cambia la reference ad Organization questi devono essere rimossi

    public void addUser(User user){
        if (!this.possibleAssigneeUsers.contains(user)){
            this.possibleAssigneeUsers.add(user);
        }
    }

    public void addUsers(ArrayList<User> users){
        this.mergeWithoutRepetition(this.possibleAssigneeUsers, users);
    }

    private void mergeWithoutRepetition(ArrayList<User> startingArrayList, ArrayList<User> arrayListToMerge){
        for (User user : arrayListToMerge){
            if (!startingArrayList.contains(user)){
                this.possibleAssigneeUsers.add(user);
            }
        }
    }
}

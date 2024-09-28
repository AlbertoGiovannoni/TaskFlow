package com.example.taskflow.DomainModel.FieldDefinitionPackage;
import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.taskflow.DomainModel.User;

public class AssigneeDefinition extends FieldDefinition {
    @DBRef
    private ArrayList<User> possibleAssigneeUsers;

    public AssigneeDefinition() {
        super();
        this.type = FieldType.ASSIGNEE;
    }

    public AssigneeDefinition(String uuid) {
        super(uuid);
        this.type = FieldType.ASSIGNEE;
    }

    public AssigneeDefinition(String uuid, String name) {
        super(uuid, name, FieldType.ASSIGNEE);
        this.possibleAssigneeUsers = new ArrayList<>();
    }

    public AssigneeDefinition(String uuid, String name, ArrayList<User> users) {
        super(uuid, name, FieldType.ASSIGNEE);
        this.possibleAssigneeUsers = users;
    }
    
    @Override
    public void addSingleEntry(Object obj){
        if (obj != null){
            if (obj instanceof User){
                if (this.possibleAssigneeUsers == null){
                    this.possibleAssigneeUsers = new ArrayList<>();
                }
                if (!this.possibleAssigneeUsers.contains((User)obj)){
                    this.possibleAssigneeUsers.add((User)obj);
                }
            }
        }
    }

    @Override
    public void addMultipleEntry(ArrayList<?> objs){
        if (this.possibleAssigneeUsers == null){
            this.possibleAssigneeUsers = new ArrayList<>();
        }
        this.mergeWithoutRepetition(this.possibleAssigneeUsers, this.castToUser(objs));
    }

    @Override
    public void removeEntry(Object obj) {
        if (obj != null){
            if (obj instanceof User){
                if (this.possibleAssigneeUsers != null){
                    if (!this.possibleAssigneeUsers.isEmpty()){
                        this.possibleAssigneeUsers.add((User)obj);
                    }
                }
            }
        }
    }

    @Override
    public void removeMultipleEntry(ArrayList<?> objs) {
        for (Object obj : objs){
            this.removeEntry(obj);
        }
    }

    private void mergeWithoutRepetition(ArrayList<User> startingArrayList, ArrayList<User> arrayListToMerge){
        for (User user : arrayListToMerge){
            if (!startingArrayList.contains(user)){
                this.possibleAssigneeUsers.add(user);
            }
        }
    }

    private ArrayList<User> castToUser(ArrayList<?> objs){
        ArrayList<User> users = new ArrayList<>();

        for (Object obj : objs){
            if (obj != null){
                if (obj instanceof User){
                    users.add((User)obj);
                }
            }
        }

        return users;
    }

    @Override
    public void reset() {
        this.possibleAssigneeUsers.clear();
    }

    @Override
    public ArrayList<?> getAllEntries() {
        return this.possibleAssigneeUsers;
    }

    @Override
    public Object getSingleEntry() {
        User user = null;

        if (this.possibleAssigneeUsers != null){
            if (!this.possibleAssigneeUsers.isEmpty()){
                user = this.possibleAssigneeUsers.get(0);
            }
        }

        return user;
    }

    @Override
    public boolean validateValue(Object obj) {
        boolean validation = false;
        if (obj != null){
            if (obj instanceof User){
                if (this.possibleAssigneeUsers != null){
                    validation = this.possibleAssigneeUsers.contains((User)obj);
                }
            }
        }
        return validation;
    }

    public ArrayList<User> getPossibleAssigneeUsers() {
        return possibleAssigneeUsers;
    }

    public void setPossibleAssigneeUsers(ArrayList<User> possibleAssigneeUsers) {
        this.possibleAssigneeUsers = possibleAssigneeUsers;
    }
}

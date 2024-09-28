package com.example.taskflow.DomainModel.FieldDefinitionPackage;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.taskflow.DomainModel.User;

public class AssigneeDefinition extends FieldDefinition {
    @DBRef
    private ArrayList<User> possibleAssigneeUsers;

    public AssigneeDefinition() {
        super();
    }

    public AssigneeDefinition(String uuid) {
        super(uuid);
    }

    public AssigneeDefinition(String uuid, String name) {
        super(uuid, name, FieldType.ASSIGNEE);
        this.possibleAssigneeUsers = new ArrayList<>();
    }

    public AssigneeDefinition(String uuid, String name, ArrayList<User> users) {
        super(uuid, name, FieldType.ASSIGNEE);
        this.possibleAssigneeUsers = users;
    }

    private void mergeWithoutRepetition(ArrayList<User> startingArrayList, ArrayList<User> arrayListToMerge) {
        for (User user : arrayListToMerge) {
            if (!startingArrayList.contains(user)) {
                this.possibleAssigneeUsers.add(user);
            }
        }
    }

    private ArrayList<User> castToUser(ArrayList<?> objs) {
        ArrayList<User> users = new ArrayList<>();

        for (Object obj : objs) {
            if (obj != null) {
                if (obj instanceof User) {
                    users.add((User) obj);
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
    public boolean validateValue(Object obj) {
        boolean validation = false;
        if (obj != null) {
            if (obj instanceof User) {
                if (this.possibleAssigneeUsers != null) {
                    validation = this.possibleAssigneeUsers.contains((User) obj);
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

    public void addAssignee(User usr) {
        this.possibleAssigneeUsers.add(usr);
    }

    public void addMultipleAssignee(ArrayList<User> usrs) {
        for (User u : usrs) {
            this.addAssignee(u);
        }
    }

    public void removeAssignee(User user) {
        if (user != null) {
            if (this.possibleAssigneeUsers != null) {
                if (!this.possibleAssigneeUsers.isEmpty()) {
                    this.possibleAssigneeUsers.add(user);
                }
            }

        }
    }

    public void removeMultipleAssignee(ArrayList<User> users) {
        for (User user : users) {
            this.removeAssignee(user);
        }
    }
}

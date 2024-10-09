package com.unifi.taskflow.domainModel.fieldDefinitions;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.unifi.taskflow.domainModel.User;

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
    }

    public AssigneeDefinition(String uuid, String name, ArrayList<User> users) {
        super(uuid, name, FieldType.ASSIGNEE);
    }

    @Override
    public void reset() {
        if (this.possibleAssigneeUsers != null){
            this.possibleAssigneeUsers.clear();
        }
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
        if (this.possibleAssigneeUsers == null){
            this.possibleAssigneeUsers = new ArrayList<>();
        }
        if (!(this.possibleAssigneeUsers.contains(usr))){
            this.possibleAssigneeUsers.add(usr);
        }
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

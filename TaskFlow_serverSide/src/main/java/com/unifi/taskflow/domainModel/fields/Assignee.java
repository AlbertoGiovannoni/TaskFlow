package com.unifi.taskflow.domainModel.fields;

import java.util.ArrayList;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.AssigneeDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;

public class Assignee extends Field {

    @DBRef
    private ArrayList<User> users;

    public Assignee(){
        super();
    }

    public Assignee(String uuid){
        super(uuid);
    }

    public Assignee(String uuid, FieldDefinition fieldDefinition, ArrayList<User> users) {
        super(uuid, fieldDefinition);

        this.users = users;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        ArrayList<User> validatedUsers = new ArrayList<>();

        for (User user : users){
            if (this.fieldDefinition.validateValue(user)){
                validatedUsers.add(user);
            }
            else{
                throw new IllegalArgumentException(user + " not allowed by AssigneeDefinition. Allowed users: " + ((AssigneeDefinition) this.fieldDefinition).getPossibleAssigneeUsers());
            }
        }

        this.users = validatedUsers;
    }
}

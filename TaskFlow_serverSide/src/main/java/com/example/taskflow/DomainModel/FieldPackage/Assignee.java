package com.example.taskflow.DomainModel.FieldPackage;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Assignee extends Field {

    @DBRef
    private ArrayList<User> users;

    public Assignee(){
    }

    public Assignee(FieldDefinition fieldDefinition, ArrayList<User> values) {
        super(fieldDefinition);

        this.users = values;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Object getValue() {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + " doesn't implement method "
                + methodName);
    }

    @Override
    public ArrayList<?> getValues() {
        return this.users;
    }

    @Override
    public void removeValue(Object value) {
        if (value != null) {
            if (value instanceof User) {
                if (this.users.contains((User) value)) {
                    this.users.remove((User) value);
                }
            }
        } else {
            throw new IllegalArgumentException("Value is null: \n" + value);
        }
    }

    @Override
    public void removeValues(ArrayList<?> values) {
        for (Object value : values) {
            this.removeValue(value);
        }
    }

    @Override
    public void reset() {
        this.users.clear();
    }

    @Override
    public void addValue(Object value) {
        if (value != null) {
            if (value instanceof User) {
                if (this.fieldDefinition.validateValue(value)) {
                    this.addUserIfNotInList((User) value);
                } else {
                    throw new IllegalArgumentException((User) value +
                            " cannot be added if corresponding AssigneeDefinition doesn't have it as a selection: \n" +
                            this.fieldDefinition.getAllEntries());
                }
            } else {
                throw new IllegalArgumentException("Value is not of type User: \n" + value);
            }
        } else {
            throw new IllegalArgumentException("Value is null: \n" + value);
        }
    }

    private void addUserIfNotInList(User user) {
        if (users == null){
            users = new ArrayList<User>();
        }
        if (!this.users.contains(user)) {
            this.users.add(user);
        }
    }

    @Override
    public void addValues(ArrayList<?> values) {
        for (Object value : values) {
            this.addValue(value);
        }
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            if (value instanceof User) {
                this.reset();
                this.addValue(value);
            }
        }
    }

    @Override
    public void setValues(ArrayList<?> values) {
        boolean valuesAreUsers = this.checkArrayListValidity(values);

        if (valuesAreUsers) {
            if (users != null) {
                this.reset();
            }
            for (Object value : values) {
                if (this.fieldDefinition.validateValue(value)) {
                    this.addUserIfNotInList((User) value);
                } else {
                    throw new IllegalArgumentException((User) value +
                            " cannot be added if corresponding AssigneeDefinition doesn't have it as a selection: \n" +
                            this.fieldDefinition.getAllEntries());
                }
            }
        }
    }

    private boolean checkArrayListValidity(ArrayList<?> values) {
        boolean valuesAreUsers = true;

        if (values != null) {
            for (Object value : values) {
                if (value != null) {
                    if (!(value instanceof User)) {
                        valuesAreUsers = false;
                    }
                } else {
                    throw new IllegalArgumentException("Value is not of type User: \n" + value);
                }
            }
        } else {
            throw new IllegalArgumentException("Values is null: \n" + users);
        }

        return valuesAreUsers;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}

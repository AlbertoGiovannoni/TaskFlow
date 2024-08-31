package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Assignee extends Field{

    private ArrayList<User> values;

    public Assignee(FieldDefinition fieldDefinition, ArrayList<User> values) {
        super(fieldDefinition);

        this.values = values;
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public ArrayList<?> getValues() {
        return this.values;
    }

    @Override
    public void removeValue(Object value) {
        if (value != null){
            if (value instanceof User){
                if (this.values.contains((User)value)){
                    this.values.remove((User)value);
                }
            }
        }
    }

    @Override
    public void removeValues(ArrayList<?> values) {
        for (Object value : values){
            this.removeValue(value);
        }
    }

    @Override
    public void reset() {
        this.values.clear();
    }

    @Override
    public void addValue(Object value) {
        if (value != null){
            if (value instanceof User){
                if (this.fieldDefinition.validateValue(value)){
                    this.addUserIfNotInList((User)value);
                }
                else{
                    throw new IllegalArgumentException((User)value + 
                                            " cannot be added if corresponding AssigneeDefinition doesn't have it as a selection: \n" + 
                                            this.fieldDefinition.getAllEntries());
                }
            }
        }
    }

    private void addUserIfNotInList(User user){
        if (!this.values.contains(user)){
            this.values.add(user);
        }
    }

    @Override
    public void addValues(ArrayList<?> values) {
        for (Object value : values){
            this.addValue(value);
        }
    }

    @Override
    public void setValue(Object value) {
        if (value != null){
            if (value instanceof User){
                this.reset();
                this.addValue(value);
            }
        }
    }

    @Override
    public void setValues(ArrayList<?> values) {
        boolean valuesAreUsers = this.checkArrayListValidity();
        
        if (valuesAreUsers){
            this.reset();
            for (Object value : values){
                if (this.fieldDefinition.validateValue(value)){
                    this.addUserIfNotInList((User)value);
                }
                else{
                    throw new IllegalArgumentException((User)value + 
                                            " cannot be added if corresponding AssigneeDefinition doesn't have it as a selection: \n" + 
                                            this.fieldDefinition.getAllEntries());
                }
            }
        }
    }

    private boolean checkArrayListValidity(){
        boolean valuesAreUsers = true;
        
        if (values != null){
            for (Object value : values){
                if (value != null){
                    if (!(value instanceof User)){
                        valuesAreUsers = false;
                    }
                }
            }
        }

        return valuesAreUsers;
    }
}

package com.example.taskflow.DomainModel.FieldPackage;

import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Date extends Field{
    
    private DateData dateData;
    
    // costruttore di default
    public Date() {
    }

    public Date(FieldDefinition fieldDefinition, DateData dateData) {
        super(fieldDefinition);
        this.dateData = dateData;
    }

    public Notification getNotification() {
        return this.dateData.getNotification();
    }

    public void setNotification(Notification notification) {
        this.dateData.setNotification(notification);
    }

    public void removeNotification(){
        this.dateData.removeNotification();
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Object getValue() {
        return this.dateData;
    }

    @Override
    public void reset() {
        this.dateData = null;
    }

    @Override
    public void setValue(Object value) {
        if (value != null){
            if (value instanceof DateData){
                this.dateData = (DateData)value;
            }
            else{
                throw new IllegalArgumentException("Value is not of type DateData: " + value);
            }
        }
        else{
            throw new IllegalArgumentException("Value is null: " + value);
        }
    }

    @Override
    public void removeValue(Object value) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public ArrayList<?> getValues() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void removeValues(ArrayList<?> values) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void addValue(Object value) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void addValues(ArrayList<?> values) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void setValues(ArrayList<?> values) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }
    
}

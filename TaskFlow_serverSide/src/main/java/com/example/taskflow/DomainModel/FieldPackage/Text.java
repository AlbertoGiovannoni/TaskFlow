package com.example.taskflow.DomainModel.FieldPackage;

import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Text extends Field{

    private String value;
    private UUID uuid;

    // costruttore di default
    public Text(){
    }

    public Text(FieldDefinition fieldDefinition, String value) {
        super(fieldDefinition);

        this.value = value;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void reset() {
        this.value = "";
    }

    @Override
    public void setValue(Object value) {
        if (value != null){
            if (value instanceof String){
                this.value = (String)value;
            }
        }
    }

    @Override
    public ArrayList<?> getValues() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void removeValue(Object value) {
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

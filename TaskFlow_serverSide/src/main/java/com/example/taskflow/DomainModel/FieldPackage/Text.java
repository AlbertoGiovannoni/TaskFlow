package com.example.taskflow.DomainModel.FieldPackage;

import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Text extends Field{

    private String value;

    // costruttore di default
    public Text(){
    }

    public Text(FieldDefinition fieldDefinition, String value) {
        super(fieldDefinition);

        this.value = value;
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
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public void removeValue(Object value) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public void removeValues(ArrayList<?> values) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public void addValue(Object value) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public void addValues(ArrayList<?> values) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public void setValues(ArrayList<?> values) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }
}

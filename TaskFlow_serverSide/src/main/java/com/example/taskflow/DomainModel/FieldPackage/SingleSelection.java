package com.example.taskflow.DomainModel.FieldPackage;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.data.annotation.TypeAlias;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

@TypeAlias("SingleSelection")
public class SingleSelection extends Field {

    private String value;

    // costruttore di default
    public SingleSelection() {
    }

    public SingleSelection(FieldDefinition fieldDefinition, String value) {
        super(fieldDefinition);

        this.value = value;
    }

    @Override
    public Object getValue() {
        return this.value;
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            if (value instanceof String) {
                this.value = (String) value;
            }
        } else {
            throw new IllegalArgumentException("Value is not of type String: \n" + value);
        }
    }

    @Override
    public void setValues(ArrayList<?> values) {
        setValue(values.get(0));
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
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public ArrayList<?> getValues() {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + " doesn't implement method "
                + methodName);
    }

    @Override
    public void reset() {
        this.value = "";
    }
}

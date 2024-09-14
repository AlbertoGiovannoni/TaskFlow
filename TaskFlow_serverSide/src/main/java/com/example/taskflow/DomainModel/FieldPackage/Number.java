package com.example.taskflow.DomainModel.FieldPackage;
import java.util.ArrayList;
import java.util.UUID;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Number extends Field{

    private Float value;

    // costruttore di default
    public Number(){
    }

    public Number(FieldDefinition fieldDefinition, Float value) {
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
    public void removeValue(Object value) {
        this.value = Float.NaN;
    }

    @Override
    public void reset() {
        this.value = Float.NaN;
    }

    @Override
    public void setValue(Object value) {
        if (value != null){
            if (value instanceof Float){
                this.value = (Float)value;
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

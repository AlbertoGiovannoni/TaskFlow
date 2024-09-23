package com.example.taskflow.DomainModel.FieldDefinitionPackage;

import java.util.ArrayList;

import java.lang.UnsupportedOperationException;

public class SimpleFieldDefinition extends FieldDefinition {
    public SimpleFieldDefinition() {
        super();
    }

    public SimpleFieldDefinition(String uuid) {
        super(uuid);
    }

    public SimpleFieldDefinition(String uuid, String name, FieldType type) {
        super(uuid, name, type);
    }

    @Override
    public void reset() {
        this.name = "";
    }

    @Override
    public void addSingleEntry(Object obj) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public void addMultipleEntry(ArrayList<?> obj) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public void removeEntry(Object obj) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public void removeMultipleEntry(ArrayList<?> objs) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public ArrayList<?> getAllEntries() {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public Object getSingleEntry() {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }

    @Override
    public boolean validateValue(Object obj) {
        String methodName = new Throwable().getStackTrace()[0].getMethodName();

        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                + methodName);
    }
}
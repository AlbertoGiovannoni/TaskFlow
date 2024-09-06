package com.example.taskflow.DomainModel.FieldDefinitionPackage;

import java.util.ArrayList;

import java.lang.UnsupportedOperationException;

public class SimpleFieldDefinition extends FieldDefinition {
    public SimpleFieldDefinition() {}

    public SimpleFieldDefinition(String name, FieldType type) {
        super(name, type);
    }

    @Override
    public void reset() {
        this.name = "";
    }

    @Override
    public void addSingleEntry(Object obj) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                );
    }

    @Override
    public void addMultipleEntry(ArrayList<?> obj) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " );
    }

    @Override
    public void removeEntry(Object obj) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                );
    }

    @Override
    public void removeMultipleEntry(ArrayList<?> objs) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                );
    }

    @Override
    public ArrayList<?> getAllEntries() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                );
    }

    @Override
    public Object getSingleEntry() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                );
    }

    @Override
    public boolean validateValue(Object obj) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() 
                                                + " doesn't implement method " 
                                                );
    }
}
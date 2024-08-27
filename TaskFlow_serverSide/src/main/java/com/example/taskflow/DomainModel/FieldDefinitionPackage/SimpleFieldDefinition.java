package com.example.taskflow.DomainModel.FieldDefinitionPackage;

import java.util.ArrayList;

import java.lang.UnsupportedOperationException;

public class SimpleFieldDefinition extends FieldDefinition {
    public SimpleFieldDefinition() {}

    public SimpleFieldDefinition(String name, FieldType type) {
        super(name, type);
    }

    @Override
    public void addSingleEntry(Object obj) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't implement method " + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void addMultipleEntry(ArrayList<Object> obj) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't implement method " + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void removeEntry(Object obj) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't implement method " + this.getClass().getEnclosingMethod().toString());
    }

    @Override
    public void removeMultipleEntry(ArrayList<Object> objs) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " doesn't implement method " + this.getClass().getEnclosingMethod().toString());
    }
}
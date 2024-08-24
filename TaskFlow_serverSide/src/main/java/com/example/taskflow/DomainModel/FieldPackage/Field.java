package com.example.taskflow.DomainModel.FieldPackage;

public abstract class Field<T> {
    
    private T value;

    public Field(T value) {
        this.value = value;
    }

    // getter e setter
    
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

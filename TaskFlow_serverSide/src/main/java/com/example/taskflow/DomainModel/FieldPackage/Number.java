package com.example.taskflow.DomainModel.FieldPackage;

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

    public Number(FieldDefinition fieldDefinition, Integer value) {
        super(fieldDefinition);

        this.value = (float)value;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public void setValue(Integer value) {
        this.value = (float)value;
    }
}

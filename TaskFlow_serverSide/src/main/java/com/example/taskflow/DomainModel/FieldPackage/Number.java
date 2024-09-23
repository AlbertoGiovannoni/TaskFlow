package com.example.taskflow.DomainModel.FieldPackage;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Number extends Field{

    private Float value;

    // costruttore di default
    public Number(){
        super();
    }

    public Number(String uuid){
        super(uuid);
    }

    public Number(String uuid, FieldDefinition fieldDefinition, Float value) {
        super(uuid, fieldDefinition);

        this.value = value;
    }

    public Number(String uuid, FieldDefinition fieldDefinition, Integer value) {
        super(uuid, fieldDefinition);

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

package com.example.taskflow.DomainModel.FieldPackage;

import java.math.BigDecimal;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Number extends Field{

    private BigDecimal value;

    // costruttore di default
    public Number(){
        super();
    }

    public Number(String uuid){
        super(uuid);
    }

    public Number(String uuid, FieldDefinition fieldDefinition, BigDecimal value) {
        super(uuid, fieldDefinition);

        this.value = value;
    }

    // public Number(String uuid, FieldDefinition fieldDefinition, Integer value) {
    //     super(uuid, fieldDefinition);

    //     this.value = (BigDecimal)value;
    // }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    // public void setValue(Integer value) {
    //     this.value = (float)value;
    // }
}

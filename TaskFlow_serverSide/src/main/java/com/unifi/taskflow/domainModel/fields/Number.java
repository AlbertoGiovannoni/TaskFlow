package com.unifi.taskflow.domainModel.fields;

import java.math.BigDecimal;

import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;

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

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

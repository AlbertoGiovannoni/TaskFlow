package com.example.taskflow.DTOs.Field;

import java.math.BigDecimal;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class NumberDTO extends FieldDTO {
    private BigDecimal value;
    
    public NumberDTO(){
        this.setType(FieldType.NUMBER);
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

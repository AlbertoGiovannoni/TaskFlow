package com.example.taskflow.DTOs.Field;

import java.math.BigDecimal;

public class NumberDTO extends FieldDTO {
    private BigDecimal value;

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

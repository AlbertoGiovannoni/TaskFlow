package com.unifi.taskflow.domainModel.fields.fieldBuilders;

import java.math.BigDecimal;

import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.Number;

public class NumberBuilder extends FieldBuilder {
    private BigDecimal value;

    public NumberBuilder(FieldDefinition fieldDefinition) {
        super(fieldDefinition);
    }

    public NumberBuilder addParameter(BigDecimal value) {
        if (value != null) {
            this.value = value;
        } else {
            throw new IllegalArgumentException("value is null:" + value);
        }
        return this;
    }

    @Override
    public Field build() {
        if (this.value == null) {
            throw new IllegalAccessError("value is null");
        }
        Number number = EntityFactory.getNumber();

        number.setFieldDefinition(this.fieldDefinition);
        number.setValue(this.value);

        return number;
    }
}
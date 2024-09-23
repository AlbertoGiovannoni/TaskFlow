package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.util.UUID;

import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Number;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class NumberBuilder extends FieldBuilder {
    private Float value;

    public NumberBuilder(FieldDefinition fieldDefinition) {
        super(fieldDefinition);
    }

    public NumberBuilder addParameter(Float value) {
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
        Number number = new Number(UUID.randomUUID().toString());

        number.setFieldDefinition(this.fieldDefinition);
        number.setValue(this.value);

        return number;
    }
}
package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Number;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import java.util.ArrayList;

public class NumberBuilder extends FieldBuilder {
    private Float value;

    NumberBuilder(FieldType type) {
        super(type);
    }

    @Override
    public FieldBuilder addParameters(ArrayList<?> values) {
        return this.addParameter(values.get(0));
    }

    @Override
    public NumberBuilder addParameter(Object value) {
        if (value != null) {
            if (value instanceof Float) {
                this.value = (Float) value;
            } else {
                throw new IllegalArgumentException("value is not of type Float:" + value);
            }
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
        return new Number(this.fieldDefinition, this.value);
    }
}
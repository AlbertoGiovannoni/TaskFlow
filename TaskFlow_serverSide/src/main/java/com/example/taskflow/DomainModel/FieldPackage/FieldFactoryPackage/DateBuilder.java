
package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.util.ArrayList;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.DateData;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class DateBuilder extends FieldBuilder {
    private DateData dateData;

    DateBuilder(FieldType type) {
        super(type);
    }

    @Override
    public FieldBuilder addParameter(Object value) {
        if (value != null) {
            if (value instanceof DateData) {
                this.dateData = (DateData) value;
            } else {
                throw new IllegalArgumentException("value is not of type DateData:" + value);
            }
        } else {
            throw new IllegalArgumentException("value is null:" + value);
        }
        return this;
    }

    @Override
    public Field build() {
        if (this.dateData == null) {
            throw new IllegalAccessError("dateData is null: " + this.dateData);
        } else if (this.fieldDefinition == null) {
            throw new IllegalAccessError("fieldDefinition is null: " + this.fieldDefinition);
        }
        return new Date(this.fieldDefinition, this.dateData);
    }

    @Override
    public FieldBuilder addParameters(ArrayList<?> values) {
        return this.addParameter(values.get(0));
    }

}

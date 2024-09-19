
package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.time.LocalDateTime;

import java.util.ArrayList;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class DateBuilder extends FieldBuilder {
    private Notification notification;
    private LocalDateTime dateTime;

    DateBuilder(FieldType type) {
        super(type);
    }

    @Override
    public FieldBuilder addParameter(Object value) {
        if (value != null) {
            if (value instanceof Notification) {
                this.notification = (Notification) value;
            } 
            else if (value instanceof LocalDateTime) {
                this.dateTime = (LocalDateTime) value;
            }
            else {
                throw new IllegalArgumentException("value is not of type DateData:" + value);
            }
        } else {
            throw new IllegalArgumentException("value is null:" + value);
        }
        return this;
    }

    @Override
    public Field build() {
        if (this.dateTime == null) {
            throw new IllegalAccessError("dateTime is null");
        } else if (this.fieldDefinition == null) {
            throw new IllegalAccessError("fieldDefinition is null: " + this.fieldDefinition);
        }
        if (this.notification != null){
            return new Date(this.fieldDefinition, this.notification, this.dateTime);
        }
        else{
            return new Date(this.fieldDefinition, this.dateTime);
        }
    }

    @Override
    public FieldBuilder addParameters(ArrayList<?> values) {
        return this.addParameter(values.get(0));
    }

}


package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;

import java.time.LocalDateTime;

import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class DateBuilder extends FieldBuilder {

    private Notification notification;
    private LocalDateTime dateTime;

    public DateBuilder(FieldDefinition fieldDefinition) {
        super(fieldDefinition);
    }


    public FieldBuilder addParameters(LocalDateTime date, Notification notification) {
        if (date != null) {
            this.dateTime = date;

            if (notification != null) {
                this.notification = notification;
            }
        } else {
            throw new IllegalArgumentException("date is null:" + date);
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
        if (this.notification != null) {
            return new Date(this.fieldDefinition, this.notification, this.dateTime);
        } else {
            return new Date(this.fieldDefinition, this.dateTime);
        }
    }

}

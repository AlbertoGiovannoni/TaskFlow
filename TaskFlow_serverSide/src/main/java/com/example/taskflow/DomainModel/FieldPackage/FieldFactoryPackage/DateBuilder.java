
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

    public DateBuilder addDate(LocalDateTime dateTime){
        if (dateTime == null){
            throw new IllegalArgumentException("date is null:");
        }

        this.dateTime = dateTime;

        return this;
    }

    public DateBuilder addNotification(Notification notification){
        if (notification == null){
            throw new IllegalArgumentException("notification is null:");
        }

        this.notification = notification;

        return this;
    }

    @Override
    public Field build() {
        if (this.dateTime == null) {
            throw new IllegalAccessError("dateTime is null");
        }

        if (this.notification != null) {
            return new Date(this.fieldDefinition, this.notification, this.dateTime);
        } else {
            return new Date(this.fieldDefinition, this.dateTime);
        }
    }

}


package com.unifi.taskflow.domainModel.fields.fieldBuilders;

import java.time.LocalDateTime;

import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.Notification;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Date;
import com.unifi.taskflow.domainModel.fields.Field;

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

        Date date = EntityFactory.getDate();

        date.setFieldDefinition(this.fieldDefinition);
        date.setDateTime(this.dateTime);

        if (this.notification != null) {
            date.setNotification(notification);
        }

        return date;
    }

}

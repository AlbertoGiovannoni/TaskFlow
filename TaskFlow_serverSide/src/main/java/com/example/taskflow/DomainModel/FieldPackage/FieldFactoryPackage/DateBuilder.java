package com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage;
import java.time.LocalDateTime;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;

public class DateBuilder extends FieldBuilder{
    private LocalDateTime date;
    private Notification notification;

    

    DateBuilder(FieldType type) {
        super(type);
    }

    @Override
    public DateBuilder setDate(LocalDateTime date){
        this.date = date;
        return this.self();
    }

    @Override
    public DateBuilder setNotification(Notification notification){
        this.notification = notification;
        return this.self();
    }

    @Override
    protected DateBuilder self() {
        return this;
    }

    @Override
    public Field build() {
        if (this.date == null){
            throw new IllegalAccessError("date is null");
        }
        if (this.notification == null){
            return new Date(this.fieldDefinition, date);
        }
        else{
            return new Date(this.fieldDefinition, this.date,  this.notification);
        }
    }
}
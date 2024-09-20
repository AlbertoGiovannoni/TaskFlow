package com.example.taskflow.DomainModel.FieldPackage;

import java.time.LocalDateTime;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Date extends Field{
    
    @DBRef
    private Notification notification;
    private LocalDateTime dateTime;
    
    // costruttore di default
    public Date() {
    }

    public Date(FieldDefinition fieldDefinition, Notification notification, LocalDateTime dateTime) {
        super(fieldDefinition);
        this.notification = notification;
        this.dateTime = dateTime;
    }

    public Date(FieldDefinition fieldDefinition, LocalDateTime dateTime) {
        super(fieldDefinition);
        this.dateTime = dateTime;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    
}

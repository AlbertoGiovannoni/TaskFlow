package com.example.taskflow.DomainModel.FieldPackage;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Date extends Field{
    
    @DBRef
    private Notification notification;
    private LocalDateTime dateTime;
    
    // costruttore di default
    public Date() {
        super();
    }

    public Date(String uuid){
        super(uuid);
    }

    public Date(String uuid, FieldDefinition fieldDefinition, Notification notification, LocalDateTime dateTime) {
        super(uuid, fieldDefinition);
        this.notification = notification;
        this.dateTime = dateTime;
    }

    public Date(String uuid, FieldDefinition fieldDefinition, LocalDateTime dateTime) {
        super(uuid, fieldDefinition);
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

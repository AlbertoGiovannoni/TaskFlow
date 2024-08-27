package com.example.taskflow.DomainModel.FieldPackage;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Date extends Field<LocalDateTime>{
    
    @DBRef
    private Notification notification;
    private Boolean enabledNotification;     // TODO usare un booleano o il fatto che c'e il campo notification e` sufficiente?
    
    // costruttore di default
    public Date() {
    }

    public Date(LocalDateTime value, FieldDefinition fieldDefinition) {
        super(value, fieldDefinition);
    }

    public Date(LocalDateTime value, FieldDefinition fieldDefinition, Notification notification, Boolean enabledNotification) {
        super(value, fieldDefinition);

        this.enabledNotification = enabledNotification;
        if (enabledNotification) {
            this.notification = notification;
        }
        else {
            this.notification =  null;
        }
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        if (this.enabledNotification) {
            this.notification = notification;
        } else {
            throw new IllegalStateException("Cannot set a notification when notifications are disabled.");
        }
    }

    public Boolean getEnabledNotification() {
        return enabledNotification;
    }

    public void setEnabledNotification(Boolean enabledNotification) {
        this.enabledNotification = enabledNotification;
        if (!enabledNotification) {
            this.notification = null;
        }    
    }
    
}

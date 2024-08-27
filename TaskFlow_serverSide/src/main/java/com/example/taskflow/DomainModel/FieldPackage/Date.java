package com.example.taskflow.DomainModel.FieldPackage;
import java.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class Date extends Field{
    
    @DBRef
    private Notification notification;
    private Boolean enabledNotification = false;
    private LocalDateTime dateTime;
    
    // costruttore di default
    public Date() {
    }

    public Date(FieldDefinition fieldDefinition, LocalDateTime dateTime) {
        super(fieldDefinition);

        this.dateTime = dateTime;
    }

    public Date(FieldDefinition fieldDefinition, LocalDateTime value, Notification notification) {
        super(fieldDefinition);

        this.notification = notification;
        this.enabledNotification = true;
    }

    public Notification getNotification() {
        return this.notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Boolean getEnabledNotification() {
        return enabledNotification;
    }

    public void removeNotification(){
        if (this.notification != null){
            this.notification = null;
        }
        this.enabledNotification = false;
    }
    
}

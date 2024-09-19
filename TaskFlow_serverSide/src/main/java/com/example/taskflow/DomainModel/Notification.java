package com.example.taskflow.DomainModel;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Notification implements UuidInterface{
    
    @Id
    private String id;
    private String message;
    private LocalDateTime notificationDateTime;
    private String uuid;

    @DBRef
    @Lazy
    ArrayList<User> receivers;

    // costruttore di default
    public Notification(){
    }

    public Notification(ArrayList<User> receivers, LocalDateTime notificationDateTime, String message) {
        this.receivers = receivers;
        this.message = message;
        this.notificationDateTime = notificationDateTime;
        this.uuid = this.createUuid();
    }

    public void addReceiver(User newUser) {
        receivers.add(newUser);
    }

    public void addReceivers(ArrayList<User> users) {
        for (User user : users){
            this.addReceiver(user);
        }
    }

    public boolean deleteReceiver(User userToRemove) {
        return receivers.remove(userToRemove);
    }

    // getter e setter

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<User> getReceivers() {
        return receivers;
    }

    public void setReceivers(ArrayList<User> receivers) {
        this.receivers = receivers;
    }

    public void setNotificationDateTime(LocalDateTime notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public boolean equals(Object obj) {
        boolean value = false;

        if (obj != null && obj instanceof Notification) {
            if (obj instanceof Notification){
                value = (this.uuid.equals(((Notification)obj).getUuid()));  
            }
        }

        return value;
    }

    public LocalDateTime getNotificationDateTime() {
        return notificationDateTime;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

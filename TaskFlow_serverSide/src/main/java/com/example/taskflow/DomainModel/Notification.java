package com.example.taskflow.DomainModel;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Notification extends BaseEntity{
    private String message;
    private LocalDateTime notificationDateTime;

    @DBRef
    @Lazy
    ArrayList<User> receivers;

    // costruttore di default
    public Notification(){
        super();
    }

    public Notification(String uuid) {
        super(uuid);
    }

    public Notification(String uuid, ArrayList<User> receivers, LocalDateTime notificationDateTime, String message) {
        super(uuid);
        this.receivers = receivers;
        this.message = message;
        this.notificationDateTime = notificationDateTime;
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

    public LocalDateTime getNotificationDateTime() {
        return notificationDateTime;
    }
}

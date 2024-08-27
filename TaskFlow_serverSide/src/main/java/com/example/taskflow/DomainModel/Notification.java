package com.example.taskflow.DomainModel;
import java.util.ArrayList;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Notification {
    
    @Id
    private String id;
    private String message;

    @DBRef
    ArrayList<User> receivers;

    // costruttore di default
    public Notification(){
    }

    public Notification(ArrayList<User> receivers, String message) {
        this.receivers = receivers;
        this.message = message;
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
    
}

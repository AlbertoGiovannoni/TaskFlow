package com.example.taskflow.DomainModel;
import java.util.ArrayList;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Notification {
    
    @Id
    private String id;
    private ArrayList<String> emails;
    private String message;

    @DBRef
    ArrayList<User> receivers;

    // costruttore di default
    public Notification(){
    }

    public Notification(ArrayList<User> receivers, String message) {
        ArrayList<String> tmpEmails = new ArrayList<String>();
        for(User r : receivers){    
            tmpEmails.add(r.getUserInfo().getEmail());
        }
        this.emails = tmpEmails;
        this.receivers = receivers;
        this.message = message;
    }

    // getter e setter

    public String getId() {
        return id;
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
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

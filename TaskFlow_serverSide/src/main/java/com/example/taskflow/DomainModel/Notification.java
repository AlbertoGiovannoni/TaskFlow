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
    private Activity activity;

    // costruttore di default
    public Notification(){
    }

    public Notification(ArrayList<String> emails, Activity activity, String message) {
        this.emails = emails;
        this.activity = activity;
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

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}

package com.example.taskflow.DomainModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "activities")
public class Activity {
    
    @Id
    private String id;
    private String name;

    // costruttore di default
    public Activity(){
    }

    public Activity(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}

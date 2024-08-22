package com.example.taskflow.DomainModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "message")
public class Message{

    @Id
    private String id;
    private String message;

    // Costruttore di default
    public Message() {
    }

    // Costruttore con parametri
    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
package com.example.taskflow.DomainModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//TODO aggiungi questa classe nel class diagram
@Document(collection = "users_info")
public class UserInfo {
    
    @Id
    private String id;
    private String username;
    private String email;
    private String password;

    // costruttore di default
    public UserInfo() {
    }

    public UserInfo(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password; // TODO: cripta password
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

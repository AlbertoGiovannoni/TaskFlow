package com.example.taskflow.DomainModel;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//TODO aggiungi questa classe nel class diagram
@Document
public class UserInfo {
    
    @Id
    private String id;
    private String email;
    private String password;
    private UUID uuid;

    // costruttore di default
    public UserInfo() {
    }

    public UserInfo(String email, String password){
        this.email = email;
        this.password = password; // TODO: cripta password
        this.uuid = UUID.randomUUID();
    }

    // getter e setter

    public String getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
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

package com.example.taskflow.DomainModel;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document
public class User implements UuidInterface{
    
    @Id
    private String id;

    @DBRef
    private UserInfo userInfo;

    private UUID uuid;
    private String username;

    // costruttore di default
    public User(){
    }

    public User(UserInfo userInfo, String username) {
        this.username = username;
        this.userInfo = userInfo;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public boolean equals(Object obj) {
        boolean value = false;

        if (obj != null && obj instanceof User){
            if (obj instanceof User){
                value = (this.uuid.equals(((User)obj).getUuid()));  
            }
        }

        return value;
    }


    // getter e setter

    public String getId() {
        return id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail(){
        return userInfo.getEmail();
    }
    
    public void setEmail(String email){
         this.userInfo.setEmail(email);
    }
    
}

package com.example.taskflow.DomainModel;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Document
public class User extends BaseEntity{

    @DBRef
    private UserInfo userInfo;

    private String username;

    // costruttore di default
    public User(){
        super();
    }

    public User(String uuid, UserInfo userInfo, String username) {
        super(uuid);
        this.username = username;
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
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

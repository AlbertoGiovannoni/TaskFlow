package com.example.taskflow.DomainModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.mongodb.core.index.Indexed;

@Document
public class UserInfo implements UuidInterface{
    
    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String uuid;

    // costruttore di default
    public UserInfo() {
    }

    public UserInfo(String email, String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        this.email = email;
        this.password = passwordEncoder.encode(password);
    }

    // getter e setter

    public String getId() {
        return id;
    }

    public String getUuid() {
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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        this.password = passwordEncoder.encode(password);
    }

    @Override
    public boolean equals(Object obj) {
        boolean value = false;

        if (obj != null && obj instanceof UserInfo){
            if (obj instanceof UserInfo){
                value = (this.uuid.equals(((UserInfo)obj).getUuid()));  
            }
        }

        return value;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

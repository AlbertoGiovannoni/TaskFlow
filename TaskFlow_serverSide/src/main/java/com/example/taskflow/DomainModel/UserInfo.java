package com.example.taskflow.DomainModel;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.mongodb.core.index.Indexed;

@Document
public class UserInfo extends BaseEntity{
    @Indexed(unique = true)
    private String email;

    private String password;

    // costruttore di default
    public UserInfo() {
        super();
    }

    public UserInfo(String uuid) {
        super(uuid);
    }

    public UserInfo(String uuid, String email, String password){
        super(uuid);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.email = email;
        this.password = passwordEncoder.encode(password);
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
}

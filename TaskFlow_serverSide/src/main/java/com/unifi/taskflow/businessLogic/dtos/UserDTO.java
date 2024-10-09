package com.unifi.taskflow.businessLogic.dtos;

import jakarta.validation.constraints.NotNull;


public class UserDTO {

    @NotNull
    private String id;   
    private String username;
    private String email;   
    
    private String uuid;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

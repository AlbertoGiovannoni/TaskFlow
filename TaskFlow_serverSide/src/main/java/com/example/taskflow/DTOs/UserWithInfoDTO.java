package com.example.taskflow.DTOs;

import javax.validation.constraints.NotNull;

public class UserWithInfoDTO {

    private String id;   
    
    @NotNull
    private String password; 
    @NotNull  
    private String name;
    @NotNull
    private String email;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }      

}

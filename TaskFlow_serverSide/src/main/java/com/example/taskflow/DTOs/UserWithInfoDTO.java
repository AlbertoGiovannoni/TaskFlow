package com.example.taskflow.DTOs;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserWithInfoDTO {

    private String id;   
    
    @Size(min = 8, message = "{validation.password.size.too_short}")
    @NotBlank(message = "Password non può essere vuota")
    private String password;

    @Size(max = 12, message = "{validation.username.size.too_long}")
    @NotBlank(message = "Username non può essere vuoto")
    private String username;

    @Email
    @NotBlank(message = "Email non può essere vuota")
    private String email;
    private String uuid;
    
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

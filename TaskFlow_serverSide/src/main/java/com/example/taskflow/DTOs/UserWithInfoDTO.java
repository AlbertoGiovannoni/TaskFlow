package com.example.taskflow.DTOs;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserWithInfoDTO {

    private String id;   
    
    @NotBlank(message = "Password non può essere vuota")
    private String password;

    @NotBlank(message = "Username non può essere vuoto")
    private String username;

    @NotBlank(message = "Email non può essere vuota")
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

}

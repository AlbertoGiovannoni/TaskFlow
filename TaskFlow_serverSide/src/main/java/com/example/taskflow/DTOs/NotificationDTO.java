package com.example.taskflow.DTOs;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public class NotificationDTO {

    private String id;
    @NotBlank(message = "Il messaggio non può essere vuoto")
    private String message;
    @NotBlank(message = "La data della notifica non può essere vuota")
    private LocalDateTime notificationDateTime;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public LocalDateTime getNotificationDateTime() {
        return notificationDateTime;
    }
    public void setNotificationDateTime(LocalDateTime notificationDateTime) {
        this.notificationDateTime = notificationDateTime;
    }
    

}

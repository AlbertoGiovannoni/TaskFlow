package com.example.taskflow.DTOs.Field;

import java.time.LocalDateTime;

import com.example.taskflow.DTOs.NotificationDTO;

import jakarta.validation.constraints.NotBlank;

public class DateDTO extends FieldDTO {

    private NotificationDTO notification; 
    @NotBlank(message = "La data della notifica non può essere vuota")
    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public NotificationDTO getNotification() {
        return notification;
    }

    public void setNotification(NotificationDTO notification) {
        this.notification = notification;
    }
}

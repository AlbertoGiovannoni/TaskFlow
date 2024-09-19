package com.example.taskflow.DTOs.Field;

import java.time.LocalDateTime;

import com.example.taskflow.DTOs.NotificationDTO;
import com.example.taskflow.DomainModel.Notification;

import jakarta.validation.constraints.NotBlank;

public class DateDTO extends FieldDTO {

    private Notification notification; //TODO usare un notificationdto?? 
    @NotBlank(message = "La data della notifica non può essere vuota")
    private LocalDateTime dateTime;

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

}

package com.example.taskflow.DomainModel.FieldPackage;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.DBRef;

import com.example.taskflow.DomainModel.Notification;

public class DateData {
    @DBRef
    private Notification notification;
    private LocalDateTime dateTime;

	public DateData(){
	}
	
    public DateData(LocalDateTime dateTime, Notification notification) {
        this.dateTime = dateTime;
        this.notification = notification;
    }

    public DateData(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

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

	public void removeNotification(){
		this.notification = null;
	}

	public boolean hasNotification(){
		boolean notificationIsSetCondition = false;
		if (this.notification != null){
			notificationIsSetCondition = true;
		}
		return notificationIsSetCondition;
	}

}

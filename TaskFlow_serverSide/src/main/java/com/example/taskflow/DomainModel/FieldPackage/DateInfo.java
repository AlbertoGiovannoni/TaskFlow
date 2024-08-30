package com.example.taskflow.DomainModel.FieldPackage;

import java.time.LocalDateTime;
import com.example.taskflow.DomainModel.Notification;

public class DateInfo {
    private LocalDateTime value;
    private Notification notification;

    public DateInfo(LocalDateTime value, Notification notification) {
        this.notification = notification;
        this.value = value;
    }

    public DateInfo(LocalDateTime value) {
        this.value = value;
    }

	public LocalDateTime getValue() {
		return value;
	}

	public void setValue(LocalDateTime value) {
		this.value = value;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

}

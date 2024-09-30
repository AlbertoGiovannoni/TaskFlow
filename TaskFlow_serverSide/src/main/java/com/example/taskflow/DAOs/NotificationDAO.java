package com.example.taskflow.DAOs;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.taskflow.DomainModel.Notification;

public interface NotificationDAO extends MongoRepository<Notification, String> {
    @Query("{'notificationDateTime': {$gte: ?0, $lt: ?1}}")
    List<Notification> findExpiringNotifications(LocalDateTime start, LocalDateTime end);
}

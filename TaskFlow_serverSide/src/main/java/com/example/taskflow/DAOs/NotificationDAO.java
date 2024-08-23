package com.example.taskflow.DAOs;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskflow.DomainModel.Notification;

public interface NotificationDAO extends MongoRepository<Notification, String> {
    
}

package com.example.taskflow.repositories;

import com.example.taskflow.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}
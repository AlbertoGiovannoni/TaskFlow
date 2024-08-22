package com.example.taskflow.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.taskflow.DomainModel.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
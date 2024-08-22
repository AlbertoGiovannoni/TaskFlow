package com.example.taskflow.DAOs;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.taskflow.DomainModel.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
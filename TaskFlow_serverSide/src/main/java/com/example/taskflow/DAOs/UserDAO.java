package com.example.taskflow.DAOs;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskflow.DomainModel.User;

public interface UserDAO extends MongoRepository<User, String> {
}
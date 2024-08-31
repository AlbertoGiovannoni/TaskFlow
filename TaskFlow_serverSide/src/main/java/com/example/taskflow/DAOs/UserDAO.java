package com.example.taskflow.DAOs;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskflow.DomainModel.User;

public interface UserDAO extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
package com.unifi.taskflow.daos;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.unifi.taskflow.domainModel.User;

public interface UserDAO extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
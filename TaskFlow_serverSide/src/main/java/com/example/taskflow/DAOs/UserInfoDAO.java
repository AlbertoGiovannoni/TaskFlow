package com.example.taskflow.DAOs;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskflow.DomainModel.UserInfo;

public interface UserInfoDAO extends MongoRepository<UserInfo, String> {
    Optional<UserInfo> findByEmail(String email);
}



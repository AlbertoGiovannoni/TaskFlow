package com.unifi.taskflow.daos;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.unifi.taskflow.domainModel.UserInfo;

public interface UserInfoDAO extends MongoRepository<UserInfo, String> {
    Optional<UserInfo> findByEmail(String email);
}



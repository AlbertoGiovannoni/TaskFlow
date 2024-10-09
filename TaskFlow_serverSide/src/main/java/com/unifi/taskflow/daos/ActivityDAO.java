package com.unifi.taskflow.daos;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.unifi.taskflow.domainModel.Activity;

public interface ActivityDAO extends MongoRepository<Activity, String>, CustomActivityDAO {
    
}

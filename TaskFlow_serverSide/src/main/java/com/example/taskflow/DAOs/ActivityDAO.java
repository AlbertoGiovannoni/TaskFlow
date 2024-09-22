package com.example.taskflow.DAOs;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskflow.DomainModel.Activity;

public interface ActivityDAO extends MongoRepository<Activity, String>, ActivityCustomInterfaceDAO {
    
}

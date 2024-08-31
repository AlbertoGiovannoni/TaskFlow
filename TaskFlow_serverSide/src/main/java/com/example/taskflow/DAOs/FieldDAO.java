package com.example.taskflow.DAOs;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskflow.DomainModel.FieldPackage.Field;

public interface FieldDAO extends MongoRepository<Field, String> {
}

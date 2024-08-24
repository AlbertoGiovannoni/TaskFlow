package com.example.taskflow.DAOs;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public interface FieldDefinitionDAO extends MongoRepository<FieldDefinition, String> {
}

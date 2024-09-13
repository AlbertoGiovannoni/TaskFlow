package com.example.taskflow.DAOs;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;

public interface FieldDAO extends MongoRepository<Field, String> {
    public void deleteFieldByFieldDefinition(FieldDefinition fieldDefinition);
}

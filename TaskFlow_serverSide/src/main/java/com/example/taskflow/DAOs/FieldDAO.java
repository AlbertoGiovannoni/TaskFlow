package com.example.taskflow.DAOs;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Field;

public interface FieldDAO extends MongoRepository<Field, String> {
    void deleteFieldByFieldDefinition(FieldDefinition fieldDefinition);
    List<Field> findFieldByFieldDefinition(FieldDefinition fieldDefinition);
}

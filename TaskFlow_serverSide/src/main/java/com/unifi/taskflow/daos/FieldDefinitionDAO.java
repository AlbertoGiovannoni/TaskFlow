package com.unifi.taskflow.daos;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;

public interface FieldDefinitionDAO extends MongoRepository<FieldDefinition, String> {
}

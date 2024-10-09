package com.unifi.taskflow.daos;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.unifi.taskflow.domainModel.Notification;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Date;
import com.unifi.taskflow.domainModel.fields.Field;

public interface FieldDAO extends MongoRepository<Field, String> {
    void deleteFieldByFieldDefinition(FieldDefinition fieldDefinition);
    List<Field> findFieldByFieldDefinition(FieldDefinition fieldDefinition);
    List<Date> findByNotification(Notification notification);
}

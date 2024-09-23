package com.example.taskflow.DAOs;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.taskflow.DomainModel.Activity;

@Repository
public class ActivityCustomDAO implements ActivityCustomInterfaceDAO{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void deleteFieldFromActivity(String fieldId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fields.$id").is(fieldId));

        Update update = new Update().pull("fields", new org.bson.types.ObjectId(fieldId));

        mongoTemplate.updateFirst(query, update, Activity.class);
    }

    @Override
    public void deleteFieldsFromActivities(List<String> fieldIds) {
        List<ObjectId> objectIds = fieldIds.stream()
                                           .map(ObjectId::new)
                                           .collect(Collectors.toList());

        Query query = new Query();
        query.addCriteria(Criteria.where("fields.$id").in(objectIds));

        Update update = new Update().pull("fields", new Query(Criteria.where("$id").in(objectIds)));

        mongoTemplate.updateMulti(query, update, Activity.class);
    }
    
}

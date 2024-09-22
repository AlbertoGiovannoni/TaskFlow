package com.example.taskflow.DAOs;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.taskflow.DomainModel.Activity;

public class ActivityCustomDAO implements ActivityCustomInterfaceDAO{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void removeFieldFromActivity(String fieldId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fields.$id").is(fieldId));

        Update update = new Update().pull("fields", new org.bson.types.ObjectId(fieldId));

        mongoTemplate.updateMulti(query, update, Activity.class);
    }
    
}

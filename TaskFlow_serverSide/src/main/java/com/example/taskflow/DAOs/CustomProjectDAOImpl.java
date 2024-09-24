package com.example.taskflow.DAOs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.taskflow.DomainModel.Activity;

public class CustomProjectDAOImpl implements CustomProjectDAO{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void removeFieldDefinitionFromProject(String fieldDefinitionId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fieldsTemplate.$id").is(fieldDefinitionId));

        Update update = new Update().pull("fieldsTemplate", new org.bson.types.ObjectId(fieldDefinitionId));

        mongoTemplate.updateFirst(query, update, Activity.class);
    }
}

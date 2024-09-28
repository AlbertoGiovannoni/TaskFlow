package com.example.taskflow.DAOs;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class CustomProjectDAOImpl implements CustomProjectDAO{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void removeFieldDefinitionFromProject(String fieldDefinitionId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fieldsTemplate.$id").is(fieldDefinitionId));

        Update update = new Update().pull("fieldsTemplate", new ObjectId(fieldDefinitionId));
        
        this.mongoTemplate.updateFirst(query, update, FieldDefinition.class);
    }

    @Override
    public Project findProjectByFieldDefinition(String fieldDefinitionId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fieldsTemplate.$id").is(new ObjectId(fieldDefinitionId)));

        return this.mongoTemplate.findOne(query, Project.class);
    }

    @Override
    public Project findProjectByActivity(String activityId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("activities.$id").is(new ObjectId(activityId)));

        return this.mongoTemplate.findOne(query, Project.class);
    }
}

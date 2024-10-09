package com.unifi.taskflow.daos;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.unifi.taskflow.domainModel.Activity;

public class CustomActivityDAOImpl implements CustomActivityDAO {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void removeFieldFromActivity(String fieldId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("fields.$id").is(fieldId));

        Update update = new Update().pull("fields", new org.bson.types.ObjectId(fieldId));

        mongoTemplate.updateFirst(query, update, Activity.class);
    }

    @Override
    public void removeFieldsFromActivities(List<String> fieldIds) {
        List<ObjectId> objectIds = fieldIds.stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());

        Query query = new Query();
        query.addCriteria(Criteria.where("fields.$id").in(objectIds));

        Update update = new Update().pull("fields", Query.query(Criteria.where("$id").in(objectIds)));

        this.mongoTemplate.updateMulti(query, update, Activity.class);
    }

    @Override
    public ArrayList<Activity> getActivitiesByFieldIds(List<String> fieldIds) {
        List<ObjectId> objectIds = fieldIds.stream()
                                        .map(ObjectId::new)
                                        .collect(Collectors.toList());

        Query query = new Query();
        query.addCriteria(Criteria.where("fields.$id").in(objectIds));

        List<Activity> activities = mongoTemplate.find(query, Activity.class);

        return new ArrayList<>(activities);
    }

}

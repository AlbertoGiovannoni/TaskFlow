package com.unifi.taskflow.daos;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.unifi.taskflow.domainModel.Organization;

public class CustomOrganizationDAOImpl implements CustomOrganizationDAO{

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public ArrayList<Organization> getOrganizationByUser(String userId) {
        Query query = new Query();
        query.addCriteria(new Criteria().orOperator(
            Criteria.where("owners").in(userId), // Se l'utente è tra gli owner
            Criteria.where("members").in(userId) // Se l'utente è tra i membri
        ));

        List<Organization> organizations = mongoTemplate.find(query, Organization.class);
        
        return new ArrayList<Organization>(organizations);
    }

    @Override
    public Organization getOrganizationByProject(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("projects.$id").is(new ObjectId(projectId)));

        return this.mongoTemplate.findOne(query, Organization.class);
    }
    
}

package com.example.taskflow;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Test;

import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;

import net.bytebuddy.utility.RandomString;

@Component
public class TestUtil {

    @Autowired
    MongoTemplate template;
    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;

    public void cleanDatabase(){
        Set<String> allCollections = this.template.getCollectionNames();
        
        for (String collectionName : allCollections){
            this.template.dropCollection(collectionName);
        }
    }

    public FieldDefinition pushGetRandomFieldDefinitionToDatabase(FieldType type){
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(type)
                                    .setName(RandomString.make(10))
                                    .build();

        return this.fieldDefinitionDAO.save(fieldDefinition);
    }
}

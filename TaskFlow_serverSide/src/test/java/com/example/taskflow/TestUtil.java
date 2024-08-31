package com.example.taskflow;

import java.util.ArrayList;
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
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;

import net.bytebuddy.utility.RandomString;

@Component
public class TestUtil {

    @Autowired
    private MongoTemplate template;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private UserDAO userDAO;

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

    public User addGetRandomUserToDatabase(){
        UserInfo info = new UserInfo(RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(info);

        User user = new User(info, RandomString.make(10));
        return this.userDAO.save(user);
    }

    public ArrayList<User> addGetMultipleRandomUserToDatabase(int n){
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < n; i++){
            users.add(this.addGetRandomUserToDatabase());
        }

        return users;
    }
}

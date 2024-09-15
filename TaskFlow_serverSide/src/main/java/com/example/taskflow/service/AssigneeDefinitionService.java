package com.example.taskflow.service;

import java.util.ArrayList;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

public class AssigneeDefinitionService extends FieldDefinitionService{
    public FieldDefinition addPartecipants(String idFieldDefinition, ArrayList<String> userIds){
        User user;
        FieldDefinition fieldDefinitionFromDatabase = this.fieldDefinitionDao
                                                        .findById(idFieldDefinition)
                                                        .orElseThrow();
        if (!(fieldDefinitionFromDatabase instanceof AssigneeDefinition)){
            throw new IllegalArgumentException("Expected " + AssigneeDefinition.class.getSimpleName() + ", find " + fieldDefinitionFromDatabase.getClass().getSimpleName());
        }

        for (String userId : userIds){
            user = this.userDao.findById(userId).orElse(null);
            if (user != null){
                fieldDefinitionFromDatabase.addSingleEntry(user);
            }
        }

        return this.fieldDefinitionDao.save(fieldDefinitionFromDatabase);
    }
}

package com.example.taskflow.service.FieldDefinitionServices;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.FieldDefinition.AssigneeDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.Mappers.FieldDefinitionMapper;

import com.example.taskflow.DTOs.FieldDefinition.SingleSelectionDefinitionDTO;


@Service
public class FieldDefinitionService {
    @Autowired
    FieldDefinitionDAO fieldDefinitionDao;

    @Autowired
    FieldDAO fieldDao;

    @Autowired
    UserDAO userDao;

    @Autowired
    FieldDefinitionMapper fieldDefinitionMapper;
    
    public FieldDefinitionDTO createFieldDefinition(FieldDefinitionDTO fieldDefinitionDto){

        FieldDefinitionBuilder fieldDefinitionBuilder = FieldDefinitionFactory.getBuilder(fieldDefinitionDto.getType())
                                                                .setName(fieldDefinitionDto.getName());

        switch(fieldDefinitionDto.getType()){
            case ASSIGNEE:
                ArrayList<User> users = this.getUsersById(((AssigneeDefinitionDTO)fieldDefinitionDto).getUserIds());
                fieldDefinitionBuilder.addParameters(users);
            case SINGLE_SELECTION:
                fieldDefinitionBuilder.addParameters(((SingleSelectionDefinitionDTO)fieldDefinitionDto).getSelections());       
            default:
                ;
        }

        FieldDefinition newFieldDefinition = fieldDefinitionBuilder.build();

        FieldDefinition fieldDefinitionFromDatabase = this.fieldDefinitionDao.save(newFieldDefinition);
        
        return this.fieldDefinitionMapper.toDto(fieldDefinitionFromDatabase);
    }

    private ArrayList<User> getUsersById(ArrayList<String> userIds){
        ArrayList<User> users = new ArrayList<>();
        User movingUser;

        for (String id : userIds){
            movingUser = this.userDao.findById(id).orElse(null);
            if (movingUser != null){
                users.add(movingUser);
            }
        }

        return users;
    }
}

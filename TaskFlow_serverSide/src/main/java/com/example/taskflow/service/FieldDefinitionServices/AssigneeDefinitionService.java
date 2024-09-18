package com.example.taskflow.service.FieldDefinitionServices;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.FieldDefinition.AssigneeDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;

@Service
public class AssigneeDefinitionService extends FieldDefinitionService{
    @Autowired
    private UserDAO userDao;

    @Override
    public FieldDefinitionDTO createFieldDefinition(FieldDefinitionDTO fieldDefinitionDto) {
        if (fieldDefinitionDto == null){
            throw new IllegalArgumentException("FieldDefinitionDTO is null");
        }
        if (!(fieldDefinitionDto instanceof AssigneeDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinitionDto of class " + fieldDefinitionDto.getClass().getSimpleName() + " instead of AssigneeDefinitionDTO");
        }
        
        AssigneeDefinitionDTO assigneeDefinitionDTO = (AssigneeDefinitionDTO)fieldDefinitionDto;
        
        FieldDefinition fieldDefinitionCreated = FieldDefinitionFactory.getBuilder(assigneeDefinitionDTO.getType())
                                                                    .setName(assigneeDefinitionDTO.getName())
                                                                    .addParameters(this.getUsersById(assigneeDefinitionDTO.getPossibleAssigneeUserIds()))
                                                                    .build();

        FieldDefinition fieldDefinitionFromDatabase = this.fieldDefinitionDao.save(fieldDefinitionCreated);

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
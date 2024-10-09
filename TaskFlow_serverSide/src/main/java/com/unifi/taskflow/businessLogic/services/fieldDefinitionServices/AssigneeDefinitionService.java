

package com.unifi.taskflow.businessLogic.services.fieldDefinitionServices;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.AssigneeDefinitionDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.FieldDefinitionDTO;
import com.unifi.taskflow.daos.OrganizationDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.Organization;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.AssigneeDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders.AssigneeDefinitionBuilder;

@Service
public class AssigneeDefinitionService extends FieldDefinitionService{
    @Autowired
    private UserDAO userDao;
    @Autowired
    private OrganizationDAO organizationDao;

    @Transactional
    @Override
    public FieldDefinition pushNewFieldDefinition(FieldDefinitionDTO fieldDefinitionDto) {
        if (fieldDefinitionDto == null){
            throw new IllegalArgumentException("FieldDefinitionDTO is null");
        }
        if (!(fieldDefinitionDto instanceof AssigneeDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinitionDto of class " + fieldDefinitionDto.getClass().getSimpleName() + " instead of AssigneeDefinitionDTO");
        }
        
        AssigneeDefinitionDTO assigneeDefinitionDTO = (AssigneeDefinitionDTO)fieldDefinitionDto;
        
        AssigneeDefinitionBuilder builder = new AssigneeDefinitionBuilder();

        Organization organization = this.organizationDao.findById(assigneeDefinitionDTO.getOrganizationId()).orElse(null);

        if (organization == null){
            throw new IllegalArgumentException("Organization in DTO not found");
        }

        builder.setOrganization(organization);

        if (assigneeDefinitionDTO.getAssigneeIds() != null){
            builder.setUsers(this.getUsersById(assigneeDefinitionDTO.getAssigneeIds()));
        }

        FieldDefinition fieldDefinitionCreated = builder.setName(assigneeDefinitionDTO.getName())
                                                            .build();

        FieldDefinition fieldDefinitionFromDatabase = this.fieldDefinitionDao.save(fieldDefinitionCreated);

        return fieldDefinitionFromDatabase;
    }

    private ArrayList<User> getUsersById(ArrayList<String> userIds){
        ArrayList<User> users = new ArrayList<>();

        if (userIds != null){
            User movingUser;
    
            for (String id : userIds){
                movingUser = this.userDao.findById(id).orElse(null);
                if (movingUser != null){
                    users.add(movingUser);
                }
            }
        }

        return users;
    }

    public FieldDefinitionDTO addUsers(String fieldDefinitionId, ArrayList<String> userIds){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }

        if (!(fieldDefinition instanceof AssigneeDefinition)){
            throw new IllegalArgumentException(fieldDefinitionId + " is an Id of " + fieldDefinition.getClass().getSimpleName() + " instead of " + AssigneeDefinition.class.getSimpleName());
        }

        ArrayList<User> users = new ArrayList<>(this.userDao.findAllById(userIds));

        ((AssigneeDefinition)fieldDefinition).addMultipleAssignee(users);

        this.fieldDefinitionDao.save(fieldDefinition);

        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    }

    public FieldDefinitionDTO removeUsers(String fieldDefinitionId, ArrayList<String> userIds){
        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionId).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }

        if (!(fieldDefinition instanceof AssigneeDefinition)){
            throw new IllegalArgumentException(fieldDefinitionId + " is an Id of " + fieldDefinition.getClass().getSimpleName() + " instead of " + AssigneeDefinition.class.getSimpleName());
        }

        ArrayList<User> users = new ArrayList<>(this.userDao.findAllById(userIds));

        ((AssigneeDefinition)fieldDefinition).removeMultipleAssignee(users);

        this.fieldDefinitionDao.save(fieldDefinition);

        return this.fieldDefinitionMapper.toDto(fieldDefinition);
    }

    @Transactional
    @Override
    public FieldDefinitionDTO updateFieldDefinition(FieldDefinitionDTO fieldDefinitionDto) {
        if (!(fieldDefinitionDto instanceof AssigneeDefinitionDTO)){
            throw new IllegalArgumentException("FieldDefinition is not AssigneeDefinitionDTO");
        }
        
        AssigneeDefinitionDTO assigneeDefinitionDto = (AssigneeDefinitionDTO) fieldDefinitionDto;

        FieldDefinition fieldDefinition = this.fieldDefinitionDao.findById(fieldDefinitionDto.getId()).orElse(null);

        if (fieldDefinition == null){
            throw new IllegalArgumentException("FieldDefinition not found");
        }
        
        if (!(fieldDefinition instanceof AssigneeDefinition)){
            throw new IllegalArgumentException("FieldDefinition with id: " + fieldDefinition.getId() + " is not of type AssigneeDefinition");
        }

        AssigneeDefinition assigneeDefinition = (AssigneeDefinition)fieldDefinition;

        if (assigneeDefinitionDto.getName() != null){
            assigneeDefinition.setName(fieldDefinitionDto.getName());
        }

        if (assigneeDefinitionDto.getAssigneeIds() != null){
            assigneeDefinition.setPossibleAssigneeUsers(
                new ArrayList<User> (this.userDao.findAllById(
                    assigneeDefinitionDto.getAssigneeIds())
                )
            );
        }

        this.fieldDefinitionDao.save(assigneeDefinition);

        return this.fieldDefinitionMapper.toDto(assigneeDefinition);
    }
    
}
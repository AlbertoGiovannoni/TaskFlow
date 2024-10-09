package com.unifi.taskflow.businessLogic.services.fieldServices;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.field.AssigneeDTO;
import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.mappers.FieldMapper;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Assignee;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.AssigneeBuilder;

@Service
public class AssigneeService extends FieldService {

    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    FieldMapper fieldMapper;
    @Autowired
    FieldDAO fieldDao; 
    @Autowired
    UserDAO userDAO;
    
    @Override
    public Field pushNewField(FieldDTO fieldDto){

        if (!(fieldDto instanceof AssigneeDTO)){
            throw new IllegalArgumentException("FieldDto of class " + fieldDto.getClass().getSimpleName() + " instead of AssigneeDTO");
        }

        AssigneeDTO assigneeDTO = (AssigneeDTO)fieldDto;

        FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(assigneeDTO.getFieldDefinitionId())
                .orElse(null);

        if (fieldDefinition == null) {
            throw new IllegalArgumentException("Wrong fieldDefinition id");
        }

        Field createdField;
        if (assigneeDTO.getUserIds() != null){
            ArrayList<User> users = this.getUsersByIds(assigneeDTO.getUserIds());
            createdField = (new AssigneeBuilder(fieldDefinition))
                            .addAssignees(users)
                            .build();
        }
        else{
            createdField = (new AssigneeBuilder(fieldDefinition))
                .build();
        }

        createdField = fieldDao.save(createdField);

        return createdField;
    }

    private ArrayList<User> getUsersByIds(ArrayList<String> ids) {
        ArrayList<User> users = new ArrayList<User>();
        User usr;

        for (String id : ids){
            usr = userDAO.findById(id).orElse(null);
            if (usr != null){
                users.add(usr);
            }
        }

        return users;
    }

    @Override
    public Field updateField(FieldDTO fieldDto) {
        Assignee field = (Assignee) this.fieldDao.findById(fieldDto.getId()).orElse(null);

        if (field == null){
            throw new IllegalArgumentException("Field assignee not found");
        }

        AssigneeDTO assigneeDTO = (AssigneeDTO)fieldDto;

        ArrayList<String> ids = assigneeDTO.getUserIds();
        ArrayList<User> newUsers = new ArrayList<User>();

        newUsers = (ArrayList<User>) this.userDAO.findAllById(ids);
        field.setUsers(newUsers);

        this.fieldDao.save(field);

        return field;
    }

}

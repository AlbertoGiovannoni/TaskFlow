package com.example.taskflow.service.FieldService;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;
import com.example.taskflow.Mappers.FieldMapper;

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

        ArrayList<User> users = this.getUsersByIds(assigneeDTO.getUserIds());

        Field field = FieldFactory.getBuilder(FieldType.ASSIGNEE)
                .addFieldDefinition(fieldDefinition)
                .addParameters(users)
                .build();

        field = fieldDao.save(field);

        return field;
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

}

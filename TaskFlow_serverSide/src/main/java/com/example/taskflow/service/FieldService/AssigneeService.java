package com.example.taskflow.service.FieldService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldFactory;
import com.example.taskflow.Mappers.FieldMapper;
import com.example.taskflow.Mappers.UserMapper;

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
    public FieldDTO createField(FieldDTO fieldDto){

        if (!(fieldDto instanceof AssigneeDTO)){
            throw new IllegalArgumentException("FieldDto of class " + fieldDto.getClass().getSimpleName() + " instead of AssigneeDTO");
        }

        AssigneeDTO assigneeDTO = (AssigneeDTO)fieldDto;

        FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(assigneeDTO.getFieldDefinitionId())
                .orElse(null);

        if (fieldDefinition == null) {
            throw new IllegalArgumentException("Wrong fieldDefinition id");
        }

        FieldType fieldType = fieldDefinition.getType();

        Field f = fieldMapper.toEntity(fieldDto);
        f.setFieldDefinition(fieldDefinition);

        ArrayList<String> ids = assigneeDTO.getValuesDto();
        ArrayList<User> users = new ArrayList<User>();
        User usr;

        for (String id : ids){
            usr = userDAO.findById(id).orElse(null);
            if (usr == null){
                throw new IllegalArgumentException("User id not found");
            }
            users.add(usr);
        }

        f.setValues(users);

        Field field = FieldFactory.getBuilder(fieldType)
                .addFieldDefinition(fieldDefinition)
                .addParameters(f.getValues())
                .build();

        field = fieldDao.save(field);


        return fieldMapper.toDto(field);
    }
}

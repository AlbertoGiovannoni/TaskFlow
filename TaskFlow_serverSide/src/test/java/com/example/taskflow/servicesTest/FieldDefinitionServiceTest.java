package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DTOs.Field.StringDTO;
import com.example.taskflow.DTOs.FieldDefinition.AssigneeDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SingleSelectionDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionServiceManager;

import net.bytebuddy.utility.RandomString;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class FieldDefinitionServiceTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private FieldDefinitionServiceManager fieldDefinitionServiceManager;

    @Autowired 
    private FieldDefinitionDAO fieldDefinitionDao;

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
    }

    @Test
    public void testCreation(){
        FieldDefinitionDTO fieldDefinitionDto;
        FieldDefinition createdFieldDefinition;

        for (FieldType type : FieldType.values()){
            fieldDefinitionDto = this.getFieldDefinitionDTO(type);
            fieldDefinitionDto.setName(RandomString.make(10));

            createdFieldDefinition = this.fieldDefinitionServiceManager
                                                        .getFieldDefinitionService(fieldDefinitionDto)
                                                        .pushNewFieldDefinition(fieldDefinitionDto);
        
            assertEquals(createdFieldDefinition, this.fieldDefinitionDao.findById(createdFieldDefinition.getId()).orElse(null));
        }
    }

    private FieldDefinitionDTO getFieldDefinitionDTO(FieldType type){
        FieldDefinitionDTO fieldDefinitionDTO;
        switch (type) {
            case ASSIGNEE:
                fieldDefinitionDTO =  new AssigneeDefinitionDTO();
                break;
            case SINGLE_SELECTION:
                fieldDefinitionDTO =  new SingleSelectionDefinitionDTO();
                break;
            default:
                fieldDefinitionDTO =  new SimpleFieldDefinitionDTO();
                break;
        }
        fieldDefinitionDTO.setType(type);
        return fieldDefinitionDTO;
    }
}

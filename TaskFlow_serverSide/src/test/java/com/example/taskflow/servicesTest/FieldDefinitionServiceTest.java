package com.example.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.Mappers.FieldDefinitionMapper;
import com.example.taskflow.service.FieldDefinitionServices.FieldDefinitionService;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;

import net.bytebuddy.utility.RandomString;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class FieldDefinitionServiceTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private FieldDefinitionService fieldDefinitionService;

    @Autowired 
    private FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    private FieldDAO fieldDao;

    @Autowired
    private FieldDefinitionMapper fieldDefinitionMapper;

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
    }

    @Test
    public void testCreation(){
        FieldDefinitionDTO fieldDefinitionDto = new SimpleFieldDefinitionDTO();

        fieldDefinitionDto.setName(RandomString.make(10));
        fieldDefinitionDto.setType(FieldType.DATE);

        FieldDefinitionDTO createdFieldDefinitionDto = this.fieldDefinitionService.createFieldDefinition(fieldDefinitionDto);

        FieldDefinition createdFieldDefinition = this.fieldDefinitionMapper.toEntity(createdFieldDefinitionDto);
        
        assertEquals(createdFieldDefinition, this.fieldDefinitionDao.findById(createdFieldDefinitionDto.getId()).orElse(null));
    }
}

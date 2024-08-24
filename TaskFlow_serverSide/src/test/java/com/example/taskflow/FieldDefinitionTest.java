package com.example.taskflow;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.FieldDefinitionDAO;

@DataMongoTest
@ActiveProfiles("test")
public class FieldDefinitionTest {

    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;

    @Test
    public void testInsertAndFindFieldDefinition() {
        
        FieldDefinition fieldDefinition = FieldDefinitionBuilder.buildField(FieldType.DATE, "scadenza");

        fieldDefinition = fieldDefinitionDAO.save(fieldDefinition);

        FieldDefinition found = fieldDefinitionDAO.findById(fieldDefinition.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(fieldDefinition.getName(), found.getName());
    }
    
}

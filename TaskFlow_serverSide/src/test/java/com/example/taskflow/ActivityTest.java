package com.example.taskflow;

import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;

@DataMongoTest
@ActiveProfiles("test")
public class ActivityTest {

    @Autowired
    private ActivityDAO activityDAO;

    @Autowired
    private FieldDefinitionDAO fieldDefinitionDAO;

    @Test
    public void testInsertAndFindActivity() {

        // creazione e inserimento attività
        ArrayList<Field> fields = new ArrayList<Field>();
        FieldDefinition fieldDefinition = FieldDefinitionBuilder.buildField(FieldType.DATE, "meeting");
        FieldDefinition fieldDefinition2 = FieldDefinitionBuilder.buildField(FieldType.TEXT, "testo del field di prova");
        Field<LocalDateTime> date = new Date(LocalDateTime.now(), fieldDefinition);
        Field<String> text = new Text("CIAOOOOOO", fieldDefinition2);
        
        fieldDefinitionDAO.save(fieldDefinition);
        fieldDefinitionDAO.save(fieldDefinition2);
        
        fields.add(date);
        fields.add(text);

        Activity activity = new Activity("test1", fields);

        activity = activityDAO.save(activity);

        Activity found = activityDAO.findById(activity.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(activity.getName(), found.getName());
        assertEquals(activity.getUuid(), found.getUuid());
        //assertEquals(1, found.getActivities().size());
    }
}

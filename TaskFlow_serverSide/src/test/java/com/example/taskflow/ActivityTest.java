package com.example.taskflow;

import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.ProjectDAO;

@DataMongoTest
@ActiveProfiles("test")
public class ActivityTest {

    @Autowired
    private ActivityDAO activityDAO;

    @Test
    public void testInsertAndFindProject() {

        // creazione e inserimento attività
        ArrayList<Field> fields = new ArrayList<Field>();
        Field<LocalDateTime> date = new Date(LocalDateTime.now());
        Field<String> text = new Text("CIAOOOOOO");
        fields.add(date);
        fields.add(text);

        Activity activity = new Activity("Riunione", fields);

        activity = activityDAO.save(activity);

        Activity found = activityDAO.findById(activity.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(activity.getName(), found.getName());
        assertEquals(activity.getUuid(), found.getUuid());
        //assertEquals(1, found.getActivities().size());
    }
}

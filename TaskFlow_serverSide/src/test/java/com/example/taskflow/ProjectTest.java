package com.example.taskflow;
import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.ProjectDAO;

@DataMongoTest
@ActiveProfiles("test")
public class ProjectTest {

    @Autowired
    private ProjectDAO projectDAO;

    @Autowired
    private ActivityDAO activityDAO;

    @Test
    public void testInsertAndFindProject() {

        // creazione e inserimento attività
        ArrayList<Field> fields = new ArrayList<Field>();
        ArrayList<Activity> activities = new ArrayList<Activity>();
        Activity activity = new Activity("Riunione", fields);

        activity = activityDAO.save(activity);
        activities.add(activity);

        ArrayList<FieldDefinition> fieldsTemplate = new ArrayList<FieldDefinition>();

        // creazione e inserimento progetto
        Project project = new Project("SWAM", fieldsTemplate, activities);
        project = projectDAO.save(project);

        Project found = projectDAO.findById(project.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(project.getName(), found.getName());
        assertEquals(project.getUuid(), found.getUuid());
        assertEquals(1, found.getActivities().size());
    }
    
}

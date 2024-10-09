package com.unifi.taskflow;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.NotificationDAO;
import com.unifi.taskflow.domainModel.Notification;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldType;
import com.unifi.taskflow.domainModel.fieldDefinitions.SingleSelectionDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.fieldDefinitionBuilders.AssigneeDefinitionBuilder;
import com.unifi.taskflow.domainModel.fields.Assignee;
import com.unifi.taskflow.domainModel.fields.Date;
import com.unifi.taskflow.domainModel.fields.Document;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.Number;
import com.unifi.taskflow.domainModel.fields.SingleSelection;
import com.unifi.taskflow.domainModel.fields.Text;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.AssigneeBuilder;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.DateBuilder;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.DocumentBuilder;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.NumberBuilder;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.SingleSelectionBuilder;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.TextBuilder;

import net.bytebuddy.utility.RandomString;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class FieldTest {
    @Autowired
    private TestUtil testUtil;

    @Autowired
    private FieldDAO fieldDao;
    @Autowired
    private FieldDefinitionDAO fieldDefinitionDao;
    @Autowired
    private NotificationDAO notificationDao;

    @BeforeEach
    public void setupDatabase() {
        this.testUtil.cleanDatabase();
    }

    @Test
    public void testTextField() {
        FieldDefinition fieldDefinition = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.TEXT);

        Field field = (new TextBuilder(fieldDefinition))
                .addText(RandomString.make(10))
                .build();

        Field fieldFromDB = this.fieldDao.save(field);
        Field found = this.fieldDao.findById(field.getId()).orElse(null);

        assertEquals(field, found);
        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);

        String randomString = RandomString.make(10);
        ((Text) fieldFromDB).setValue(randomString);

        Text fieldModified = (Text) this.fieldDao.save(fieldFromDB);

        assertEquals(fieldModified.getValue(), randomString);
        assertEquals(fieldModified.getUuid(), fieldFromDB.getUuid());
        assertEquals(field.getUuid(), fieldFromDB.getUuid());
    }

    @Test
    public void testNumberField() {
        FieldDefinition fieldDefinition = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.NUMBER);

        Field field = (new NumberBuilder(fieldDefinition))
                .addParameter(BigDecimal.valueOf(Math.random()))
                .build();

        Number fieldFromDB = (Number) this.fieldDao.save(field);

        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);

        BigDecimal randomFloat = BigDecimal.valueOf(Math.random());
        fieldFromDB.setValue(randomFloat);

        Number fieldModified = (Number) this.fieldDao.save(fieldFromDB);

        assertEquals(fieldModified.getValue(), randomFloat);
        assertEquals(fieldModified.getUuid(), fieldFromDB.getUuid());
        assertEquals(field.getUuid(), fieldFromDB.getUuid());
    }

    @Test
    public void testSingleSelectionField() {
        FieldDefinition fieldDefinition = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.SINGLE_SELECTION);

        ArrayList<String> someSelections = new ArrayList<>();
        ArrayList<String> subsetOfSomeSelections = new ArrayList<>();
        ArrayList<String> someSelectionsNotValid = new ArrayList<>();

        String randomString = RandomString.make(10);

        for (int i = 0; i < 10; i++) {
            someSelections.add(randomString);
            someSelectionsNotValid.add(RandomString.make(10));
            if (i % 2 == 0) {
                subsetOfSomeSelections.add(randomString);
            }
        }

        ((SingleSelectionDefinition) fieldDefinition).addMultipleSelection(someSelections);
        this.fieldDefinitionDao.save(fieldDefinition);

        Field field = (new SingleSelectionBuilder(fieldDefinition))
                .addSelection(someSelections.get(1))
                .build();

        SingleSelection fieldFromDB = (SingleSelection) this.fieldDao.save(field);

        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);

        fieldFromDB.setValue(subsetOfSomeSelections.get(0));

        assertEquals(field, fieldFromDB);
        assertEquals(fieldFromDB.getFieldDefinition(), fieldDefinition);

        assertEquals(fieldFromDB.getValue(), subsetOfSomeSelections.get(0));

        assertEquals(field, this.fieldDao.save(fieldFromDB));

        assertThrows(IllegalArgumentException.class, () -> {
            fieldFromDB.setValue("a string not accepted");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            fieldFromDB.setValue(someSelectionsNotValid.get(0));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            fieldFromDB.setValue(someSelectionsNotValid.get(1));
        });
    }

    @Test
    public void testAssigneeField() {
        ArrayList<User> someUsers = this.testUtil.addGetMultipleRandomUserToDatabase(5);
        ArrayList<User> subsetOfSomeUsers = new ArrayList<>(Arrays.asList(someUsers.get(0), someUsers.get(3)));

        FieldDefinition fieldDefinition = new AssigneeDefinitionBuilder()
                .setUsers(someUsers)
                .setName(RandomString.make(10))
                .build();

        this.fieldDefinitionDao.save(fieldDefinition);

        Field field = (new AssigneeBuilder(fieldDefinition))
                .addAssignee(someUsers.get(0))
                .build();

        Assignee fieldFromDB = (Assignee) this.fieldDao.save(field);
        Field found = this.fieldDao.findById(field.getId()).orElse(null);

        assertEquals(field, found);
        assertEquals(field.getFieldDefinition(), fieldDefinition);

        fieldFromDB.setUsers(subsetOfSomeUsers);
        assertEquals(field, fieldFromDB);

        fieldFromDB.setUsers(new ArrayList<User>());

        assertThrows(IllegalArgumentException.class, () -> {
            fieldFromDB.setUsers(this.testUtil.addGetMultipleRandomUserToDatabase(3));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            fieldFromDB.setUsers((this.testUtil.addGetMultipleRandomUserToDatabase(3)));
        });
    }

    @Test
    public void testDateField() {
        FieldDefinition fieldDefinition = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.DATE);
        ArrayList<User> someUsers = this.testUtil.addGetMultipleRandomUserToDatabase(10);
        Notification notification = new Notification(UUID.randomUUID().toString(), someUsers, LocalDateTime.now(),
                RandomString.make(10));

        this.notificationDao.save(notification);

        Field field = (new DateBuilder(fieldDefinition))
                .addDate(LocalDateTime.now())
                .addNotification(notification)
                .build();

        Field fieldFromDB = this.fieldDao.save(field);

        assertEquals(field, fieldFromDB);
        assertEquals(field.getFieldDefinition(), fieldDefinition);
        assertEquals(((Date) fieldFromDB).getNotification(), notification);

        Notification anotherNotification = new Notification(UUID.randomUUID().toString(), someUsers,
                LocalDateTime.now(), RandomString.make(10));

        ((Date) fieldFromDB).setNotification(anotherNotification);
        assertEquals(((Date) fieldFromDB).getNotification(), anotherNotification);

        fieldDao.delete(fieldFromDB);
        assertEquals(fieldDao.findById(fieldFromDB.getId()), Optional.empty());
    }

    @Test
    public void testDocumentField() throws URISyntaxException {
        FieldDefinition fieldDefinition = this.testUtil.pushGetFieldDefinitionToDatabase(FieldType.DOCUMENT);

        // Path del file PDF
        Path pdfPath = Paths.get(getClass().getClassLoader().getResource("Integral_image.pdf").getPath());

        // Leggi il PDF e caricalo come byte[]
        byte[] content = null;
        try {
            content = Files.readAllBytes(pdfPath);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error reading the PDF file: " + e.getMessage());
        }

        Field field = (new DocumentBuilder(fieldDefinition))
                .addFileName("test")
                .addFileType("pdf")
                .addContent(content) // Inserisci il contenuto del PDF
                .build();

        Field fieldFromDB = this.fieldDao.save(field);
        assertEquals(field, fieldFromDB);


        // Aggiungi asserzioni per verificare il contenuto
        Document document = (Document) field;
        assertArrayEquals(((Document)fieldFromDB).getContent(), document.getContent()); // Verifica che il contenuto corrisponda
    }
}

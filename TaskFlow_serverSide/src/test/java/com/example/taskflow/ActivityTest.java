package com.example.taskflow;

import com.example.taskflow.DomainModel.Activity;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionBuilder;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinitionFactoryPackage.FieldDefinitionFactory;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.Text;

import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;

@DataMongoTest
@ActiveProfiles("test")
public class ActivityTest {

    @Autowired
    private ActivityDAO activityDAO;

    @Autowired
    private FieldDefinitionDAO fieldDefinitionDAO;

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    UserDAO userDAO;
    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    MongoTemplate template;

    ArrayList<User> someUsers;
    ArrayList<String> someSingleSeletions;

    @BeforeEach
    public void setupDatabase(){
        if (template.collectionExists("fieldDefinition")){
            template.dropCollection("fieldDefinition");
        }
        if (template.collectionExists("user")){
            template.dropCollection("user");
        }
        if (template.collectionExists("userInfo")){
            template.dropCollection("userInfo");
        }

        for (int i = 0; i < 5; i++){
            this.addRandomUserToDatabase();
        }

        this.someUsers = this.userDAO.findAll().stream().collect(Collectors.toCollection(ArrayList::new));

        someSingleSeletions = new ArrayList<String>();
        someSingleSeletions.add("Done");
        someSingleSeletions.add("In progress");
        someSingleSeletions.add("Waiting");
    }

    private void addRandomUserToDatabase(){
        UserInfo info = new UserInfo(RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(info);

        User user = new User(info, RandomString.make(10));
        this.userDAO.save(user);
    }

    @Test
    public void testInsertAndFindActivity() {

        // creazione e inserimento attività
        ArrayList<Field> fields = new ArrayList<Field>();
        FieldDefinition fieldDefinition = FieldDefinitionFactory.getBuilder(FieldType.DATE)
                                        .addCommonAttributes("Scadenza")
                                        .build();

        FieldDefinition fieldDefinition2 = FieldDefinitionFactory.getBuilder(FieldType.TEXT)
        .addCommonAttributes("descrizione")
        .build();

        Notification notification = new Notification(someUsers, "notifica per meeting");

        notificationDAO.save(notification);

        Field<LocalDateTime> date = new Date(LocalDateTime.now(), fieldDefinition, notification,true);
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

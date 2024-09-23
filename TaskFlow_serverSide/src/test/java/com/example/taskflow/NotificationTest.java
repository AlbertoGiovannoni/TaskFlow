package com.example.taskflow;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@DataMongoTest
@ActiveProfiles("test")
public class NotificationTest {
    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    NotificationDAO notificationDAO;

    @Autowired
    MongoTemplate template;

    ArrayList<User> someUsers;
    ArrayList<String> someSingleSelections;

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
    }

    private User addGetRandomUserToDatabase(){
        UserInfo info = new UserInfo(UUID.randomUUID().toString(), RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(info);

        User user = new User(UUID.randomUUID().toString(), info, RandomString.make(10));
        return this.userDAO.save(user);
    }

    private ArrayList<User> addGetMultipleRandomUserToDatabase(int n){
        ArrayList<User> users = new ArrayList<>();

        for (int i = 0; i < n; i++){
            users.add(this.addGetRandomUserToDatabase());
        }

        return users;
    }

    @Test
    public void testInsertAndFindNotification() {
        ArrayList<User> users = this.addGetMultipleRandomUserToDatabase(3);

        for(User user : users){
            userDAO.save(user);
        }

        Notification notification = new Notification(UUID.randomUUID().toString(), users, LocalDateTime.now(), RandomString.make(10));
        notificationDAO.save(notification);

        Notification notificationFromDB = notificationDAO.findById(notification.getId()).orElse(null);

        assertEquals(notification, notificationFromDB);
    }

    @Test
    public void testModifyNotification() {
        ArrayList<User> users = this.addGetMultipleRandomUserToDatabase(3);

        for(User user : users){
            userDAO.save(user);
        }

        Notification notification = new Notification(UUID.randomUUID().toString(), users, LocalDateTime.now(), RandomString.make(10));
        notificationDAO.save(notification);

        String newMessage = RandomString.make(10);
        notification.setMessage(newMessage);
        assertEquals(notification.getMessage(), newMessage);


        ArrayList<User> newUsers = this.addGetMultipleRandomUserToDatabase(2);
        for (User user : newUsers) {
            userDAO.save(user);
        }
        notification.setReceivers(newUsers);
        assertEquals(notification.getReceivers(), newUsers);

        User newUser = this.addGetRandomUserToDatabase();
        userDAO.save(newUser);
        notification.addReceiver(newUser);
        
        boolean found = false;
        for (User user : notification.getReceivers()) {
            if (user.equals(newUser))
                found = true;
        }
        assertTrue(found);

        found = false;
        notification.deleteReceiver(newUser);
        for (User user : notification.getReceivers()) {
            if (user.equals(newUser))
                found = true;
        }
        assertFalse(found);


        Notification notificationFromDB = notificationDAO.findById(notification.getId()).orElse(null);
        assertEquals(notification, notificationFromDB);
    
    }
}

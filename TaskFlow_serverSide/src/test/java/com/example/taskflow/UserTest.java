package com.example.taskflow;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import java.util.ArrayList;

@DataMongoTest
@ActiveProfiles("test")
public class UserTest {

    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    private TestUtil testUtil;
    @Autowired
    UserDAO userDAO;
    @Autowired
    UserInfoDAO userInfoDAO;

    @Autowired
    MongoTemplate template;

    ArrayList<User> someUsers;
    ArrayList<String> someSingleSelections;

    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
    }

    @Test
    public void testInsertAndFindUser() {

        UserInfo userinfo = new UserInfo(RandomString.make(10), RandomString.make(10));
        userInfoDAO.save(userinfo);
        
        User user = new User(userinfo, RandomString.make(10), false);
        user = userDAO.save(user);

        User found = userDAO.findById(user.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(user.getUsername(), found.getUsername());
        assertEquals(user.getUuid(), found.getUuid());
    }

    @Test
    public void testMondifyUser() {
        UserInfo userinfo = new UserInfo(RandomString.make(10), RandomString.make(10));
        userInfoDAO.save(userinfo);

        User user = new User(userinfo, RandomString.make(10), false);
        user = userDAO.save(user);

        user.setUserInfo(userinfo);

        String newUsername = RandomString.make(10);
        user.setUsername(newUsername);
        assertEquals(user.getUsername(), newUsername);

        UserInfo newUserinfo = new UserInfo(RandomString.make(10), RandomString.make(10));
        newUserinfo = userInfoDAO.save(newUserinfo);
        user.setUserInfo(newUserinfo);
        assertEquals(user.getUserInfo(), newUserinfo);

        User userPushed = this.userDAO.save(user);
        assertEquals(user.getId(), userPushed.getId());
    }
}

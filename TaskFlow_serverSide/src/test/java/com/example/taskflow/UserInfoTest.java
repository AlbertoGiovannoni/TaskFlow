package com.example.taskflow;

import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class UserInfoTest {

    @Autowired
    private TestUtil testUtil;

    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
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
    public void testInsertAndFindUserInfo() {

        UserInfo userinfo = new UserInfo(UUID.randomUUID().toString(), RandomString.make(10), RandomString.make(10));
        userInfoDAO.save(userinfo);

        UserInfo found = userInfoDAO.findById(userinfo.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(userinfo.getEmail(), found.getEmail());
        assertEquals(userinfo.getPassword(), found.getPassword());
        assertEquals(userinfo.getUuid(), found.getUuid());
    }

    @Test
    public void testModifyUserInfo() {

        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), RandomString.make(10), RandomString.make(10));
        userInfoDAO.save(userInfo);

        String newEmail = RandomString.make(10);
        userInfo.setEmail(newEmail);
        assertEquals(userInfo.getEmail(), newEmail);

        String newPassword = RandomString.make(10);
        userInfo.setPassword(newPassword);
       // assertEquals(userInfo.getPassword(), newPassword);
        
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        assertTrue(passwordEncoder.matches(newPassword, userInfo.getPassword()));

        UserInfo userInfoPushed = this.userInfoDAO.save(userInfo);
        assertEquals(userInfo.getId(), userInfoPushed.getId());
    }
}

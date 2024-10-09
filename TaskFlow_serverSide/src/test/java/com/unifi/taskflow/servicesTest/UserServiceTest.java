package com.unifi.taskflow.servicesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.unifi.taskflow.TestUtil;
import com.unifi.taskflow.businessLogic.dtos.UserDTO;
import com.unifi.taskflow.businessLogic.dtos.UserWithInfoDTO;
import com.unifi.taskflow.businessLogic.mappers.UserMapper;
import com.unifi.taskflow.businessLogic.services.UserService;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.daos.UserInfoDAO;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.UserInfo;

import net.bytebuddy.utility.RandomString;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class UserServiceTest {

    @Autowired
    private TestUtil testUtil;
    @Autowired
    private UserInfoDAO userInfoDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void setupDatabase() {
        this.testUtil.cleanDatabase();
    }

    @Test
    public void testCreateUser() {
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), RandomString.make(10), RandomString.make(10));
        User user = new User(UUID.randomUUID().toString(), userInfo, RandomString.make(10));

        UserWithInfoDTO userDto = this.userMapper.toDtoWithInfo(user);
        
        UserDTO userFromDb = this.userService.createUser(userDto);

        assertEquals(userDto.getUsername(), userFromDb.getUsername());
    }

    @Test
    public void testUpdateUser() {
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), RandomString.make(10), RandomString.make(10));
        this.userInfoDAO.save(userInfo);
        User user = new User(UUID.randomUUID().toString(), userInfo, RandomString.make(10));
        this.userDAO.save(user);

        UserWithInfoDTO userWithInfoDto = new UserWithInfoDTO();
        userWithInfoDto.setId(user.getId());
        userWithInfoDto.setEmail("new email");
        userWithInfoDto.setUsername("new username");
        userWithInfoDto.setPassword("newPsw");

        this.userService.updateUser(user.getId(), userWithInfoDto);

        user = this.userDAO.findById(user.getId()).orElseThrow();

        assertEquals(user.getEmail(), userWithInfoDto.getEmail());
        assertEquals(user.getUsername(), userWithInfoDto.getUsername());

    }

    @Test
    public void testDeleteUser() {
        UserInfo userInfo = new UserInfo(UUID.randomUUID().toString(), RandomString.make(10), RandomString.make(10));
        userInfo = userInfoDAO.save(userInfo);

        User user = new User(UUID.randomUUID().toString(), userInfo, RandomString.make(10));
        user = userDAO.save(user);

        userService.deleteUserById(user.getId());

        assertNull(userInfoDAO.findById(userInfo.getId()).orElse(null));
        assertNull(userDAO.findById(user.getId()).orElse(null));
    }
    
}

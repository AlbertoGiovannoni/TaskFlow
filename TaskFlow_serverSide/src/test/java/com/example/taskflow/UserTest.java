package com.example.taskflow;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
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
    private UserDAO userDAO;

    @Autowired
    private UserInfoDAO userInfoDAO;

    @Test
    public void testInsertAndFindUser() {

        // creazione e inserimento attività
        UserInfo userinfo = new UserInfo( "pippo@gmail.com", "trombone69");
        userInfoDAO.save(userinfo);
        
        User user = new User(userinfo,"pippo");
        user = userDAO.save(user);

        User found = userDAO.findById(user.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(user.getUsername(), found.getUsername());
        assertEquals(user.getUserInfo().getEmail(), found.getUserInfo().getEmail());
        assertEquals(user.getUserInfo().getPassword(), found.getUserInfo().getPassword());
        assertEquals(user.getUserInfo().getUuid(), found.getUserInfo().getUuid());
        assertEquals(user.getUuid(), found.getUuid());
    }
}

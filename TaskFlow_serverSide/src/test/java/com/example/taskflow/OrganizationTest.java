package com.example.taskflow;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;


@DataMongoTest
@ActiveProfiles("test")
public class OrganizationTest {

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private UserInfoDAO userInfoDAO;

    @Test
    public void testInsertAndFindOrganization() {

        // creo userInfo per l'owner
        UserInfo ui = new UserInfo("Paolo Rossi", "paolo.rosse@gmail.com", "password");
        // salvo userInfo
        ui = userInfoDAO.save(ui);

        ArrayList<User> owners = new ArrayList<>();

        // creo user e lo salvo
        User owner = new User(ui);
        owner = userDAO.save(owner);

        owners.add(owner);

        Organization organization = new Organization("Example Organization", owners, new ArrayList<>(), new ArrayList<>());
        organization.setCreationDate(LocalDateTime.now()); //TODO da includere nel costruttore??

        // salvo organizzazione
        organization = organizationDAO.save(organization);

        Organization found = organizationDAO.findById(organization.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(organization.getName(), found.getName());
        assertEquals(organization.getUuid(), found.getUuid());
        assertEquals(1, found.getOwners().size());
    }
}
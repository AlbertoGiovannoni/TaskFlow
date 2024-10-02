package com.example.taskflow.controllerTest;

import com.example.taskflow.TestUtil;
import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DAOs.UserInfoDAO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import net.bytebuddy.utility.RandomString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class userControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    TestUtil testUtil;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OrganizationDAO organizationDAO;
    @Autowired
    private UserInfoDAO userInfoDAO;

    @Test
    public void testRegister() throws Exception {
        String email = this.getRandomMail();
        String username = RandomString.make(10);
        String jsonBody = "{ \"username\": \"" + username + "\", \"password\": \"password\" , \"email\": \""
                + email + "\", \"isAdmin\": false}";

        mockMvc.perform(post("/api/public/register")
                .contentType("application/json")
                .content(jsonBody))
                .andExpect(status().isCreated());
    }

    private String getRandomMail(){
        return RandomString.make(5) + "." + RandomString.make(5) + "@gmail.com";
    }

    @Test
    public void updateUser() throws Exception {

        User user = testUtil.addGetRandomUserToDatabase();

        // Aggiorna l'utente
        String newEmail = this.getRandomMail();
        String newUsername = RandomString.make(10);
        String updateJsonBody = "{ \"username\": \"" + newUsername
                + "\", \"password\": \"newPassword\" , \"email\": \""
                + newEmail + "\", \"isAdmin\": false}";

        mockMvc.perform(patch("/api/user/" + user.getId())
                .contentType("application/json")
                .content(updateJsonBody))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "owner") // TODO per ora si usa admin fisso , si puo fare con owner?
    public void testRemoveUser() throws Exception {

        User target = testUtil.addGetRandomUserToDatabase();
        Organization organization = testUtil.addRandomOrganizationToDatabase();

        User owner = userDAO.findByUsername("owner").orElse(null);
        if (owner == null) {
            UserInfo info = new UserInfo(UUID.randomUUID().toString(),RandomString.make(10), "password");
            this.userInfoDAO.save(info);

            owner = new User(UUID.randomUUID().toString(),info, "owner");
            this.userDAO.save(owner);
        }
        organization.addOwner(owner);

        organization.addMember(target);
        organizationDAO.save(organization);
        mockMvc.perform(delete(
                "/api/user/" + owner.getId() + "/myOrganization/" + organization.getId() + "/users/" + target.getId())
                .contentType("application/json").content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdatedDelete() throws Exception{
        ArrayList<Organization> organizations = this.testUtil.getEntireDatabaseMockup(2, 10, 5, 30);
        
        Organization organization = organizations.get(0);

        User owner = organization.getOwners().get(0);
        User member = organization.getMembers().get(0);

        String message = this.mockMvc.perform(
            delete("/api/user" + owner.getId() + "/myOrganization/" + organization.getId() + "/users")
                .param("targetId", member.getId()))
            .andReturn().getResponse().getErrorMessage();

        System.out.println(message);
    }
}
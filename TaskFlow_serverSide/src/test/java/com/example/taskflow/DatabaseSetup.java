package com.example.taskflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class DatabaseSetup {
    @Autowired
    TestUtil testUtil;

    @BeforeEach
    public void cleanDatabase() {
        this.testUtil.cleanDatabase();
    }

    @Test
    public void setupDatabase(){
        int nOrganization = 2;
        int nProjectForOrganization = 3;
        int nActivitiesForProject = 10;
        int nApplicationUsers = 100;
        this.testUtil.getEntireDatabaseMockup(nOrganization, 
                                                nProjectForOrganization, 
                                                nActivitiesForProject,
                                                nApplicationUsers
        );
    }
}

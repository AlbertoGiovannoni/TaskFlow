package com.example.taskflow;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.example.taskflow.DAOs.ActivityDAO;
import com.example.taskflow.DomainModel.Activity;

import net.bytebuddy.utility.RandomString;

@DataMongoTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.example.taskflow")
public class ActivityTest {
    @Autowired
    TestUtil testUtil;
    @Autowired
    ActivityDAO activityDAO;
    
    @BeforeEach
    public void setupDatabase(){
        this.testUtil.cleanDatabase();
    }

    @Test
    public void testCreateActivity(){
        String randomName = RandomString.make(10);

        Activity activity = new Activity(randomName, this.testUtil.pushGetNumberFields(10));

        Activity activityFromDatabase = this.activityDAO.save(activity);

        assertEquals(activity, activityFromDatabase);
    }
}

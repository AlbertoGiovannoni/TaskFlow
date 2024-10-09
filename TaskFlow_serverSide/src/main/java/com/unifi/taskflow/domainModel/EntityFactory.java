package com.unifi.taskflow.domainModel;

import java.util.UUID;

import com.unifi.taskflow.domainModel.fieldDefinitions.*;
import com.unifi.taskflow.domainModel.fields.*;
import com.unifi.taskflow.domainModel.fields.Number;

public class EntityFactory {
    public static String getUuid(){
        return UUID.randomUUID().toString();
    }

    // DOMAIN MODEL
    public static Activity getActivity(){
        return new Activity(EntityFactory.getUuid());
    }

    public static Project getProject(){
        return new Project(EntityFactory.getUuid());
    }

    public static Organization getOrganization(){
        return new Organization(EntityFactory.getUuid());
    }

    public static User getUser(){
        return new User(EntityFactory.getUuid());
    }

    public static Notification getNotification(){
        return new Notification(EntityFactory.getUuid());
    }

    public static UserInfo getUserInfo(){
        return new UserInfo(EntityFactory.getUuid());
    }

    // FIELD DEFINITION
    public static AssigneeDefinition getAssigneeDefinition(){
        return new AssigneeDefinition(EntityFactory.getUuid());
    }

    public static SimpleFieldDefinition getSimpleFieldDefinition(){
        return new SimpleFieldDefinition(EntityFactory.getUuid());
    }

    public static SingleSelectionDefinition getSingleSelectionDefinition(){
        return new SingleSelectionDefinition(EntityFactory.getUuid());
    }

    // FIELD
    public static Assignee getAssignee(){
        return new Assignee(EntityFactory.getUuid());
    }

    public static Date getDate(){
        return new Date(EntityFactory.getUuid());
    }

    public static Document getDocument(){
        return new Document(EntityFactory.getUuid());
    }

    public static Number getNumber(){
        return new Number(EntityFactory.getUuid());
    }

    public static SingleSelection getSingleSelection(){
        return new SingleSelection(EntityFactory.getUuid());
    }

    public static Text getText(){
        return new Text(EntityFactory.getUuid());
    }
}

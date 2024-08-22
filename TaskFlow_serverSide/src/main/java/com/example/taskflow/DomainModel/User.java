package com.example.taskflow.DomainModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.List;

@Document(collation = "users")
public class User {
    @Id
    private String id;

    @DBRef
    private List<Organization> organizations;

    @DBRef
    private User_info userInfo;

    // costruttore di default
    public User(){
    }

    public User(List<Organization> organizations, User_info userInfo){
        this.organizations = organizations;
        this.userInfo = userInfo;
    }

    public String getId() {
        return id;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public User_info getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(User_info userInfo) {
        this.userInfo = userInfo;
    }
}

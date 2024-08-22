package com.example.taskflow.DomainModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.ArrayList;

@Document(collection = "users")
public class User {
    @Id
    private String id;

    @DBRef
    private ArrayList<Organization> organizations;

    @DBRef
    private UserInfo userInfo;

    // costruttore di default
    public User(){
    }

    public User(ArrayList<Organization> organizations, UserInfo userInfo){
        this.organizations = organizations;
        this.userInfo = userInfo;
    }

    public User(UserInfo userInfo){
        this.organizations = new ArrayList<Organization>();
        this.userInfo = userInfo;
    }
    
    public void addOrganization(Organization organization){
        this.organizations.add(organization);
    }
    
    public String getId() {
        return id;
    }

    public ArrayList<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(ArrayList<Organization> organizations) {
        this.organizations = organizations;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
    
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    
}

package com.example.taskflow.DomainModel;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.temporal.ChronoUnit;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Document
public class Organization extends BaseEntity{
    private String name;
    private LocalDateTime creationDate;

    @DBRef
    private ArrayList<User> owners = new ArrayList<User>();
    @DBRef
    private ArrayList<User> members = new ArrayList<User>();

    @DBRef
    private ArrayList<Project> projects = new ArrayList<Project>();


    // costruttore di default
    public Organization() {
        super();
    }

    public Organization(String uuid) {
        super(uuid);
    }

    public Organization(String uuid, String name, ArrayList<User> owners) {
        super(uuid);
        this.name = name;
        this.owners = owners;
    }

    
    public Organization(String uuid, String name, ArrayList<User> owners, ArrayList<Project> projects, ArrayList<User> members, LocalDateTime creationDate) {
        super(uuid);
        this.name = name;
        this.owners = owners;
        this.projects = projects;
        this.members = members;
        this.creationDate = creationDate.truncatedTo(ChronoUnit.MINUTES);
    }

    public void addMember(User user) { 
        members.add(user);
    }

    public boolean removeMember(User user) {
        return members.remove(user);
    }

    public void addProject(Project project) { 
        projects.add(project);
    }

    public boolean removeProject(Project project) {
        return projects.remove(project);
    }
    
    public void addOwner(User user) { 
        owners.add(user);
    }

    public boolean removeOwner(User user) {
        return owners.remove(user);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ArrayList<User> getOwners() {
        return this.owners;
    }

    public void setOwners(ArrayList<User> owners) {
        this.owners = owners;
    }

    public ArrayList<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public ArrayList<User> getMembers() {
        return this.members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }
}

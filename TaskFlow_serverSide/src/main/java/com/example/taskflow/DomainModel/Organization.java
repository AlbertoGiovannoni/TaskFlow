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
    private ArrayList<User> owners;
    @DBRef
    private ArrayList<User> members;

    @DBRef
    private ArrayList<Project> projects;


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
        if (this.members == null){
            this.members = new ArrayList<>();
        }
        members.add(user);
    }

    public boolean removeMember(User user) {
        if (this.members != null){
            return members.remove(user);
        }
        return false;
    }

    public void addProject(Project project) { 
        if (this.projects == null){
            this.projects = new ArrayList<>();
        }
        projects.add(project);
    }

    public boolean removeProject(Project project) {
        if (this.projects != null){
            return projects.remove(project);
        }
        return false;
    }
    
    public void addOwner(User user) { 
        if (this.owners == null){
            this.owners = new ArrayList<>();
        }
        owners.add(user);
    }

    public boolean removeOwner(User user) {
        if (this.owners != null){
            return owners.remove(user);
        }
        return false;
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
        this.creationDate = creationDate.truncatedTo(ChronoUnit.MINUTES);
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

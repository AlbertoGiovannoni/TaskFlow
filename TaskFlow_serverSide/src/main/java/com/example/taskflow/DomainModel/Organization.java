package com.example.taskflow.DomainModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Document
public class Organization {

    @Id
    private String id;
    private UUID uuid;
    private String name;
    private LocalDateTime creationDate;

    @DBRef
    private ArrayList<User> owners;

    @DBRef
    private ArrayList<Project> projects;

    @DBRef
    private ArrayList<User> members;

    // costruttore di default
    public Organization() {
    }

    public Organization(String name, ArrayList<User> owners, ArrayList<Project> projects, ArrayList<User> members) {
        this.name = name;
        this.owners = owners;
        this.projects = projects;
        this.members = members;
        this.uuid = UUID.randomUUID();
    }

    public void addMember(User user) { 
        members.add(user);
    }

    public void removeMember(User user) { // TODO aggiungere UUID e fare equals su quello prima di fare remove
        members.remove(user);
    }

    // getter e setter

    public String getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ArrayList<User> getOwners() {
        return owners;
    }

    public void setOwners(ArrayList<User> owners) {
        this.owners = owners;
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Project> projects) {
        this.projects = projects;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }
}

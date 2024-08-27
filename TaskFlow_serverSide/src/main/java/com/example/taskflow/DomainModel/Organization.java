package com.example.taskflow.DomainModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Document
public class Organization implements UuidInterface{

    @Id
    private String id;
    private UUID uuid;
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
    }

    public Organization(String name, ArrayList<User> owners, ArrayList<Project> projects, ArrayList<User> members) {
        this.name = name;
        this.owners = owners;
        this.projects = projects;
        this.members = members;
        this.creationDate = LocalDateTime.now();
        this.uuid = UUID.randomUUID();
    }

    public void addMember(User user) { 
        members.add(user);
    }

    public boolean removeMember(User user) {
        return members.remove(user);
    }

    // getter e setter

    public String getId() {
        return this.id;
    }

    public UUID getUuid() {
        return this.uuid;
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

    @Override
    public boolean equals(Object obj) {
        boolean value = false;

        if (obj != null && obj instanceof Organization){
            if (obj instanceof Organization){
                value = (this.uuid.equals(((Organization)obj).getUuid()));  
            }
        }

        return value;
    }
}

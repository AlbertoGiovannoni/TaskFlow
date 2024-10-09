package com.unifi.taskflow.businessLogic.dtos;

import java.util.ArrayList;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class OrganizationDTO {

    private String id;
    @NotBlank(message = "name must not be blank")
    private String name;
    private LocalDateTime creationDate;
    private ArrayList<String> ownersId;
    private ArrayList<String> membersId;
    private ArrayList<String> projectsId;

    private String uuid;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public ArrayList<String> getOwnersId() {
        return ownersId;
    }
    public void setOwnersId(ArrayList<String> ownersId) {
        this.ownersId = ownersId;
    }
    public ArrayList<String> getMembersId() {
        return membersId;
    }
    public void setMembersId(ArrayList<String> membersId) {
        this.membersId = membersId;
    }
    public ArrayList<String> getProjectsId() {
        return projectsId;
    }
    public void setProjectsId(ArrayList<String> projectsId) {
        this.projectsId = projectsId;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
}

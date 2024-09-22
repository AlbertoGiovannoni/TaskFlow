package com.example.taskflow.service;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.Mappers.OrganizationMapper;
import com.example.taskflow.Mappers.ProjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationDAO organizationDAO;
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProjectService projectService;

    
    // Verifica se l'utente è un OWNER in una specifica organizzazione. 
    public boolean isOwner(String organizationId, String username) {
        Optional<Organization> organization = organizationDAO.findById(organizationId);

        if (organization.isPresent()) {
            return organization.get().getOwners().stream()
                    .anyMatch(owner -> owner.getUsername().equals(username)); // anyMatch è una terminal operation che verifica se almeno un elemento nello stream soddisfa una determinata condizione.
        }
        return false;
    }

    // Verifica se l'utente è un USER in una specifica organizzazione.
    public boolean isUser(String organizationId, String username) {
        Optional<Organization> organization = organizationDAO.findById(organizationId);

        if (organization.isPresent()) {
            return organization.get().getMembers().stream()
                    .anyMatch(member -> member.getUsername().equals(username));
        }
        return false;
    }

    public OrganizationDTO createNewOrganization(OrganizationDTO organizationDTO){
        Organization organization = organizationMapper.toEntity(organizationDTO);
        
        User owner = this.userDAO.findById(organizationDTO.getOwnersId().get(0)).orElseThrow();
        if (owner == null){
            throw new IllegalArgumentException("An organization must have an owner");
        }
        organization.addOwner(owner);
        
        ArrayList<Project> projects = new ArrayList<Project>();
        organization.setProjects(projects);
        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO addMemberToOrganization(OrganizationDTO organizationDTO, UserDTO userDTO){
        Organization organization = organizationMapper.toEntity(organizationDTO);
        
        ArrayList<User> users = new ArrayList<User>();
        for(String userId:organizationDTO.getMembersId()){
            users.add(this.userDAO.findById(userId).orElseThrow());
        }
        ArrayList<User> owners = new ArrayList<User>();
        for(String userId:organizationDTO.getOwnersId()){
            owners.add(this.userDAO.findById(userId).orElseThrow());
        }
        ArrayList<Project> projects = new ArrayList<Project>();
        for(String projectId:organizationDTO.getProjectsId()){
            projects.add(this.projectDAO.findById(projectId).orElseThrow());
        }
        organization.setMembers(users);
        organization.setOwners(owners);
        organization.setProjects(projects);

        User user = this.userDAO.findById(userDTO.getId()).orElseThrow();
        if (user == null){
            throw new IllegalArgumentException("User not defined");
        }
        organization.addMember(user);
        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO addOwnerToOrganization(OrganizationDTO organizationDTO, UserDTO ownerDTO){
        Organization organization = organizationMapper.toEntity(organizationDTO);
        
        ArrayList<User> users = new ArrayList<User>();
        for(String userId:organizationDTO.getMembersId()){
            users.add(this.userDAO.findById(userId).orElseThrow());
        }
        ArrayList<User> owners = new ArrayList<User>();
        for(String userId:organizationDTO.getOwnersId()){
            owners.add(this.userDAO.findById(userId).orElseThrow());
        }
        ArrayList<Project> projects = new ArrayList<Project>();
        for(String projectId:organizationDTO.getProjectsId()){
            projects.add(this.projectDAO.findById(projectId).orElseThrow());
        }
        organization.setMembers(users);
        organization.setOwners(owners);
        organization.setProjects(projects);
        
        User owner = this.userDAO.findById(ownerDTO.getId()).orElseThrow();
        if (owner == null){
            throw new IllegalArgumentException("owner not defined");
        }
        organization.addOwner(owner);
        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    /*
    aggiunge un nuovo progetto (vuoto)
    */
    public OrganizationDTO addNewProjectToOrganization(OrganizationDTO organizationDTO, ProjectDTO projectDTO){
        Organization organization = organizationMapper.toEntity(organizationDTO);
        
        ArrayList<User> users = new ArrayList<User>();
        for(String userId:organizationDTO.getMembersId()){
            users.add(this.userDAO.findById(userId).orElseThrow());
        }
        ArrayList<User> owners = new ArrayList<User>();
        for(String userId:organizationDTO.getOwnersId()){
            owners.add(this.userDAO.findById(userId).orElseThrow());
        }
        ArrayList<Project> projects = new ArrayList<Project>();
        for(String projectId:organizationDTO.getProjectsId()){
            projects.add(this.projectDAO.findById(projectId).orElseThrow());
        }
        organization.setMembers(users);
        organization.setOwners(owners);
        organization.setProjects(projects);
        
        Project newProject = this.projectDAO.findById(projectDTO.getId()).orElseThrow();
        if (newProject == null){
            throw new IllegalArgumentException("owner not defined");
        }
        organization.addProject(newProject);
        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    public void deleteOrganization(OrganizationDTO organizationDTO){
        Organization organization = this.organizationDAO.findById(organizationDTO.getId()).orElseThrow();
        Project project;
        for(String projectId:organizationDTO.getProjectsId()){
            project = this.projectDAO.findById(projectId).orElseThrow();           
            projectService.deleteProject(project.getId());
        }
        this.organizationDAO.delete(organization);
    }
}
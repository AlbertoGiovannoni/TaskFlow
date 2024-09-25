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
    private ProjectService projectService;
    @Autowired
    private ProjectMapper projectMapper;

    
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

    public OrganizationDTO addMemberToOrganization(String organizationId, String userId){
        Organization organization = organizationDAO.findById(organizationId).orElseThrow();
    
        User user = this.userDAO.findById(userId).orElseThrow();
        if (user == null){
            throw new IllegalArgumentException("User not defined");
        }
        if(!organization.getMembers().contains(user)){
            organization.addMember(user);
            this.organizationDAO.save(organization);
        }

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO addOwnerToOrganization(String organizationId, String ownerId){
        Organization organization = organizationDAO.findById(organizationId).orElseThrow();

        User owner = this.userDAO.findById(ownerId).orElseThrow();
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
    public OrganizationDTO addNewProjectToOrganization(String organizationId, ProjectDTO projectDTO){
        Organization organization = organizationDAO.findById(organizationId).orElseThrow();
        
        this.projectService.pushNewProject(projectDTO);
        Project newProject = this.projectMapper.toEntity(projectDTO);
        organization.addProject(newProject);
        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO getOrganizationById(String organizationId){
        Organization organization = this.organizationDAO.findById(organizationId).orElseThrow();
        return organizationMapper.toDto(organization);
    }
    
    public void deleteOrganization(String organizationId){
        Organization organization = this.organizationDAO.findById(organizationId).orElseThrow();
        for(Project project:organization.getProjects()){
            project = this.projectDAO.findById(project.getId()).orElseThrow();           
            projectService.deleteProject(project.getId());
        }
        this.organizationDAO.delete(organization);
    }
}
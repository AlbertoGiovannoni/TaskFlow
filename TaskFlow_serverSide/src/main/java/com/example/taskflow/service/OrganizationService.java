package com.example.taskflow.service;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.OrganizationDTO;
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
        
        User user = this.userDAO.findById(userDTO.getId()).orElseThrow();
        if (user == null){
            throw new IllegalArgumentException("User not defined");
        }
        organization.addMember(user);
        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    public void deleteOrganization(OrganizationDTO organizationDTO){
        Organization organization = this.organizationDAO.findById(organizationDTO.getId()).orElseThrow();
        Project project;
        for(String projectId:organizationDTO.getProjectsId()){
            project = this.projectDAO.findById(projectId).orElseThrow();           
            projectService.deleteProject(projectMapper.toDto(project));
        }
        this.organizationDAO.delete(organization);
    }
}
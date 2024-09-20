package com.example.taskflow.service;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
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

    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO){
        Organization organization = organizationMapper.toEntity(organizationDTO);
        
        ArrayList<Project> projects = new ArrayList<Project>();
        organization.setProjects(projects);
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
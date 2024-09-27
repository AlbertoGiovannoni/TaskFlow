package com.example.taskflow.service;

import com.example.taskflow.DAOs.OrganizationDAO;
import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DTOs.ProjectDTO;
import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DomainModel.EntityFactory;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.Mappers.OrganizationMapper;
import com.example.taskflow.Mappers.ProjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.DateOperators.Minute;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
                    .anyMatch(owner -> owner.getUsername().equals(username)); // anyMatch è una terminal operation che
                                                                              // verifica se almeno un elemento nello
                                                                              // stream soddisfa una determinata
                                                                              // condizione.
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

    public OrganizationDTO createNewOrganization(OrganizationDTO organizationDTO) {

        Organization organization = EntityFactory.getOrganization();
        
        organization.setName(organizationDTO.getName());
        organization.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));

        if (organizationDTO.getOwnersId() == null){
            throw new IllegalArgumentException("An organization must have an owner");
        }
        if (organizationDTO.getOwnersId().isEmpty()){
            throw new IllegalArgumentException("An organization must have an owner");
        }

        organization.setOwners(new ArrayList<>(this.userDAO.findAllById(organizationDTO.getOwnersId())));

        if (organizationDTO.getMembersId() != null){
            if (!(organizationDTO.getMembersId().isEmpty())){
                organization.setMembers(new ArrayList<>(this.userDAO.findAllById(organizationDTO.getMembersId())));
            }
        }

        // FIXME: perché dobbiamo dargli una lista vuota? -> dovrei averlo fixato ma è tardi ho sonno (@TommasoBotarelli)
        ArrayList<Project> projects = new ArrayList<Project>();
        organization.setProjects(projects);

        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO addMemberToOrganization(String organizationId, String userId) {
        Organization organization = organizationDAO.findById(organizationId).orElseThrow();

        User user = this.userDAO.findById(userId).orElseThrow();
        if (user == null) {
            throw new IllegalArgumentException("User not defined");
        }
        if (!organization.getMembers().contains(user)) {
            organization.addMember(user);
            this.organizationDAO.save(organization);
        }

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO addOwnerToOrganization(String organizationId, String ownerId) {
        Organization organization = organizationDAO.findById(organizationId).orElseThrow();

        User owner = this.userDAO.findById(ownerId).orElseThrow();
        if (owner == null) {
            throw new IllegalArgumentException("owner not defined");
        }
        if (!organization.getOwners().contains(owner)) {
            organization.addOwner(owner);
            this.organizationDAO.save(organization);
        }

        return organizationMapper.toDto(organization);
    }

    /*
     * aggiunge un nuovo progetto (vuoto)
     */
    public OrganizationDTO addNewProjectToOrganization(String organizationId, ProjectDTO projectDTO) {
        Organization organization = organizationDAO.findById(organizationId).orElseThrow();

        projectDTO = this.projectService.pushNewProject(projectDTO);
        Project newProject = this.projectMapper.toEntity(projectDTO);

        organization.addProject(newProject);
        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO getOrganizationById(String organizationId) {
        Organization organization = this.organizationDAO.findById(organizationId).orElseThrow();
        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO deleteProjectFromOrganization(String organizationId, String projectId){
        Organization organization = this.organizationDAO.findById(organizationId).orElseThrow();
        Project project = this.projectDAO.findById(projectId).orElseThrow();
        if (project == null) {
            throw new IllegalArgumentException("project not defined");
        }
        organization.removeProject(project);
        projectDAO.delete(project);

        this.organizationDAO.save(organization);
        return organizationMapper.toDto(organization);
    }
    public void deleteOrganization(String organizationId) {
        Organization organization = this.organizationDAO.findById(organizationId).orElseThrow();
        ArrayList<Project> projectList = organization.getProjects();

        if (projectList != null && projectList.size() > 0) {
            for (Project project : projectList) {
                project = this.projectDAO.findById(project.getId()).orElseThrow();
                projectService.deleteProject(project.getId());
            }
        }
        this.organizationDAO.delete(organization);
    }
}
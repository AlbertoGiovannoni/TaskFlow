package com.unifi.taskflow.businessLogic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.OrganizationDTO;
import com.unifi.taskflow.businessLogic.dtos.ProjectDTO;
import com.unifi.taskflow.businessLogic.dtos.UserDTO;
import com.unifi.taskflow.businessLogic.mappers.OrganizationMapper;
import com.unifi.taskflow.businessLogic.mappers.ProjectMapper;
import com.unifi.taskflow.businessLogic.mappers.UserMapper;
import com.unifi.taskflow.daos.OrganizationDAO;
import com.unifi.taskflow.daos.ProjectDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.Organization;
import com.unifi.taskflow.domainModel.Project;
import com.unifi.taskflow.domainModel.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
    @Autowired
    private UserMapper userMapper;

    public boolean isOwner(String organizationId, String username) {
        Optional<Organization> organization = organizationDAO.findById(organizationId);

        if (organization.isPresent()) {
            return organization.get().getOwners().stream()
                    .anyMatch(owner -> owner.getUsername().equals(username));
        }
        return false;
    }

    public boolean isUser(String organizationId, String username) {
        Optional<Organization> organization = organizationDAO.findById(organizationId);

        if (organization.isPresent()) {
            return organization.get().getMembers().stream()
                    .anyMatch(member -> member.getUsername().equals(username));
        }
        return false;
    }

    public OrganizationDTO createNewOrganization(String ownerId, OrganizationDTO organizationDTO) {

        Organization organization = EntityFactory.getOrganization();
        
        organization.setName(organizationDTO.getName());

        ZonedDateTime nowInRome = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        LocalDateTime now = nowInRome.toLocalDateTime();

        organization.setCreationDate(now.truncatedTo(ChronoUnit.MINUTES));

        if (organizationDTO.getOwnersId() == null){
            ArrayList<String> owners = new ArrayList<>();
            owners.add(ownerId);
            organizationDTO.setOwnersId(owners);
        }

        ArrayList<String> owners = new ArrayList<>(organizationDTO.getOwnersId());
        if (!(owners.contains(ownerId))){
            owners.add(ownerId);
        }
        organizationDTO.setOwnersId(owners);

        organization.setOwners(new ArrayList<User>(this.userDAO.findAllById(organizationDTO.getOwnersId())));
        organization.setMembers(new ArrayList<User>());
        organization.setProjects(new ArrayList<Project>());

        if (organizationDTO.getMembersId() != null){
            if (!(organizationDTO.getMembersId().isEmpty())){
                organization.setMembers(new ArrayList<>(this.userDAO.findAllById(organizationDTO.getMembersId())));
            }
        }

        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO addMemberToOrganization(String organizationId, String userId) {
        Organization organization = organizationDAO.findById(organizationId).orElse(null);

        if (organization == null){
            throw new IllegalArgumentException("Organization not found");
        }

        User user = this.userDAO.findById(userId).orElse(null);

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
        Organization organization = organizationDAO.findById(organizationId).orElse(null);

        if (organization == null){
            throw new IllegalArgumentException("Organization not found");
        }

        User owner = this.userDAO.findById(ownerId).orElse(null);

        if (owner == null) {
            throw new IllegalArgumentException("owner not defined");
        }
        if (!organization.getOwners().contains(owner)) {
            organization.addOwner(owner);
            this.organizationDAO.save(organization);
        }

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO addNewProjectToOrganization(String organizationId, ProjectDTO projectDTO) {
        Organization organization = organizationDAO.findById(organizationId).orElse(null);

        if (organization == null){
            throw new IllegalArgumentException("Organization not found");
        }

        projectDTO = this.projectService.pushNewProject(projectDTO);
        Project newProject = this.projectMapper.toEntity(projectDTO);

        organization.addProject(newProject);
        this.organizationDAO.save(organization);

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO getOrganizationById(String organizationId) {
        Organization organization = this.organizationDAO.findById(organizationId).orElse(null);

        if (organization == null){
            throw new IllegalArgumentException("Organization not found");
        }

        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO deleteProjectFromOrganization(String organizationId, String projectId){
        Organization organization = this.organizationDAO.findById(organizationId).orElse(null);

        if (organization == null){
            throw new IllegalArgumentException("Organization not found");
        }

        Project project = this.projectDAO.findById(projectId).orElse(null);

        if (project == null) {
            throw new IllegalArgumentException("project not defined");
        }

        organization.removeProject(project);

        projectDAO.delete(project);

        this.organizationDAO.save(organization);
        return organizationMapper.toDto(organization);
    }
    
    public void deleteOrganization(String organizationId) {
        Organization organization = this.organizationDAO.findById(organizationId).orElse(null);

        if (organization == null){
            throw new IllegalArgumentException("Organization not found");
        }

        ArrayList<Project> projectList = organization.getProjects();

        if (projectList != null && projectList.size() > 0) {
            for (Project project : projectList) {
                project = this.projectDAO.findById(project.getId()).orElse(null);

                if (project == null){
                    throw new IllegalArgumentException("Project not found");
                }

                projectService.deleteProject(project.getId());
            }
        }
        this.organizationDAO.delete(organization);
    }

    public ArrayList<User> getAllOrganizationUser(String organizationId){
        ArrayList<User> allUsers = new ArrayList<>();

        Organization organization = this.organizationDAO.findById(organizationId).orElse(null);

        if (organization == null){
            throw new IllegalArgumentException("Organization not found");
        }
        
        allUsers.addAll(organization.getOwners());
        allUsers.addAll(organization.getMembers());

        return allUsers;
    }

    public ArrayList<UserDTO> getAllOrganizationUserDTO(String organizationId){
        ArrayList<User> allUsers = this.getAllOrganizationUser(organizationId);
        ArrayList<UserDTO> allUserDTOs = new ArrayList<>();

        for (User user : allUsers){
            allUserDTOs.add(this.userMapper.toDto(user));
        }

        return allUserDTOs;
    }
}
package com.example.taskflow.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import com.example.taskflow.DAOs.ProjectDAO;
import com.example.taskflow.DAOs.UserDAO;

import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class OrganizationMapper {

    @Autowired
    UserDAO userDAO;
    @Autowired
    ProjectDAO projectDAO;

    OrganizationDTO toDto(Organization organization) {
        ArrayList<String> ownersId = new ArrayList<String>();
        ArrayList<String> membersId = new ArrayList<String>();
        ArrayList<String> projectsId = new ArrayList<String>();

        for (User owner : organization.getOwners()) {
            ownersId.add(owner.getId());
        }
        for (User member : organization.getMembers()) {
            membersId.add(member.getId());
        }
        for (Project project : organization.getProjects()) {
            projectsId.add(project.getId());
        }

        OrganizationDTO organizationDTO = new OrganizationDTO(organization.getId(), organization.getName(), organization.getCreationDate(), ownersId, membersId, projectsId);
        return organizationDTO;
    };

    Organization toEntity(OrganizationDTO organizationDto) {
        

        ArrayList<User> owners = new ArrayList<User>();
        ArrayList<User> members = new ArrayList<User>();
        ArrayList<Project> projects = new ArrayList<Project>();

        for (String ownerId : organizationDto.getOwnersId()) {
            owners.add(userDAO.findById(ownerId).orElse(null));
        }
        for (String memberId : organizationDto.getMembersId()) {
            members.add(userDAO.findById(memberId).orElse(null));
        }
        for (String projectId : organizationDto.getProjectsId()) {
            projects.add(projectDAO.findById(projectId).orElse(null));
        }

        Organization organization = new Organization(organizationDto.getName(), owners, projects, members, organizationDto.getCreationDate());
        return organization;
    }
}

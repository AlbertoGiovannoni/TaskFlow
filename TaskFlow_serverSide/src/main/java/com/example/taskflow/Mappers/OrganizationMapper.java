package com.example.taskflow.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;

import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public class OrganizationMapper {

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
        
        Organization organization = new Organization();
        organization.setName(organizationDto.getName());
        organization.setName(organizationDto.getCreationDate());
        return organization;
    }
}


package com.unifi.taskflow.businessLogic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.unifi.taskflow.businessLogic.dtos.OrganizationDTO;
import com.unifi.taskflow.domainModel.Organization;
import com.unifi.taskflow.domainModel.Project;
import com.unifi.taskflow.domainModel.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@Component
public interface OrganizationMapper {

    @Mapping(source = "ownersId", target = "owners", ignore = true)
    @Mapping(source = "membersId", target = "members", ignore = true)
    @Mapping(source = "projectsId", target = "projects", ignore = true)
    @Mapping(target = "users", ignore = true)
    Organization toEntity(OrganizationDTO dto);

    
    @Named("mapUsersToIds")
    default ArrayList<String> mapUsersToIds(ArrayList<User> users) {
        if (users != null){
            return (ArrayList<String>) users.stream().map(User::getId).collect(Collectors.toList());
        }
        return null;
    }  

    @Named("mapProjectsToIds")
    default ArrayList<String> mapProjectsToIds(ArrayList<Project> projects) {
        if (projects != null){
            return (ArrayList<String>) projects.stream().map(Project::getId).collect(Collectors.toList());
        }
        return null;
    } 

    @Mapping(source = "owners", target = "ownersId", qualifiedByName = "mapUsersToIds")
    @Mapping(source = "members", target = "membersId", qualifiedByName = "mapUsersToIds")
    @Mapping(source = "projects", target = "projectsId", qualifiedByName = "mapProjectsToIds")
    OrganizationDTO toDto(Organization user);

    @Mapping(source = "owners", target = "ownersId", ignore = true)
    @Mapping(source = "members", target = "membersId", ignore = true)
    @Mapping(source = "projects", target = "projectsId", ignore = true)
    @Mapping(source = "creationDate", target = "creationDate", ignore = true)
    OrganizationDTO toSimpleDto(Organization organization);
}

package com.example.taskflow.Mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.example.taskflow.DTOs.OrganizationDTO;
import com.example.taskflow.DomainModel.Organization;
import com.example.taskflow.DomainModel.Project;
import com.example.taskflow.DomainModel.User;

@Mapper(componentModel = "spring")
@Component
public interface OrganizationMapper {

    @Mapping(source = "ownersId", target = "owners", ignore = true)
    @Mapping(source = "membersId", target = "members", ignore = true)
    @Mapping(source = "projectsId", target = "projects", ignore = true)
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
}

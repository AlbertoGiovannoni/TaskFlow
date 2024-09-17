package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DTOs.UserWithInfoDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.UserInfo;
import com.example.taskflow.service.UserService;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    // Mappa da UserWithInfoDTO a User (senza creare UserInfo)
    @Mapping(target = "userInfo", ignore = true)  // UserInfo viene gestito nel servizio
    User toEntity(UserWithInfoDTO dto);

    // Mappa da User a UserDTO
    @Mapping(source = "userInfo.email", target = "email")
    UserDTO toDto(User user);
}
package com.unifi.taskflow.businessLogic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.unifi.taskflow.businessLogic.dtos.UserDTO;
import com.unifi.taskflow.businessLogic.dtos.UserWithInfoDTO;
import com.unifi.taskflow.domainModel.User;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(target = "userInfo", ignore = true)
    @Mapping(source = "email", target = "email", ignore = true)
    User toEntity(UserWithInfoDTO dto);

    // Mappa da User a UserDTO
    UserDTO toDto(User user);

    @Mapping(source = "userInfo.password", target = "password")
    UserWithInfoDTO toDtoWithInfo(User user);
}
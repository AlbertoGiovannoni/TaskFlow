package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DTOs.UserWithInfoDTO;
import com.example.taskflow.DomainModel.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

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
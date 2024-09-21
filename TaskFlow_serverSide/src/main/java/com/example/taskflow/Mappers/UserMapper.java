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

    @Mapping(target = "uuid", ignore = true)
    @Mapping(target = "userInfo", ignore = true)
    User toEntity(UserWithInfoDTO dto);

    // Mappa da User a UserDTO
    @Mapping(source = "email", target = "email")
    UserDTO toDto(User user);
}
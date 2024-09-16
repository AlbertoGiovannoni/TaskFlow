package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.UserDTO;
import com.example.taskflow.DomainModel.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Mappatura da User a UserDTO
    @Mapping(source = "user.id", target = "id")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.userInfo.email", target = "email")  // Mappatura da UserInfo a email
    UserDTO userToUserDTO(User user); //TODO devo creare/linkare al userInfo, crea service per user per farlo e salvare il tutto

    // Mappatura da UserDTO a User
    @Mapping(target = "userInfo.email", source = "email")
    User userDTOToUser(UserDTO userDTO);
}
package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FieldDefinitionMapper {

    FieldDefinitionMapper INSTANCE = Mappers.getMapper(FieldDefinitionMapper.class);

    FieldDefinitionDTO toDto(FieldDefinition fieldDefinition);

    // Mappatura specifica per AssigneeDefinition
    @Mapping(source = "possibleAssigneeUsers", target = "possibleAssigneeUserIds", qualifiedByName = "mapUsersToIds")
    AssigneeDefinitionDTO toDto(AssigneeDefinition assigneeDefinition);

    // Mappatura specifica per SingleSelectionDefinition
    SingleSelectionDefinitionDTO toDto(SingleSelectionDefinition singleSelectionDefinition);

    // Mappatura specifica per SimpleFieldDefinition
    SimpleFieldDefinitionDTO toDto(SimpleFieldDefinition simpleFieldDefinition);

    // Metodo per convertire la lista di User in lista di String (gli ID)
    default List<String> mapUsersToIds(List<User> users) {
        return users.stream().map(User::getId).collect(Collectors.toList());
    }
}

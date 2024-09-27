package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.FieldDefinition.AssigneeDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SingleSelectionDefinitionDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;
import java.util.ArrayList;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FieldDefinitionMapper {

    FieldDefinitionMapper INSTANCE = Mappers.getMapper(FieldDefinitionMapper.class);

    default FieldDefinitionDTO toDto(FieldDefinition fieldDefinition){
        switch (fieldDefinition.getType()) {
            case ASSIGNEE:
                return this.toDto((AssigneeDefinition)fieldDefinition);
            case SINGLE_SELECTION:
                return this.toDto((SingleSelectionDefinition) fieldDefinition);
            case DATE:
                return this.toDto((SimpleFieldDefinition) fieldDefinition);
            case NUMBER:
                return this.toDto((SimpleFieldDefinition) fieldDefinition);
            case TEXT:
                return this.toDto((SimpleFieldDefinition) fieldDefinition);
            case DOCUMENT:
                // TODO document
                throw new IllegalArgumentException(fieldDefinition.getType() + " not implemented!");
            default:
                throw new IllegalArgumentException(fieldDefinition.getType() + " not recognized");
        }
    };

    default FieldDefinition toEntity(@Valid FieldDefinitionDTO fieldDefinitionDto){
        switch (fieldDefinitionDto.getType()) {
            case ASSIGNEE:
                return this.toEntity((AssigneeDefinitionDTO)fieldDefinitionDto);
            case SINGLE_SELECTION:
                return this.toEntity((SingleSelectionDefinitionDTO) fieldDefinitionDto);
            case DATE:
                return this.toEntity((SimpleFieldDefinitionDTO) fieldDefinitionDto);
            case NUMBER:
                return this.toEntity((SimpleFieldDefinitionDTO) fieldDefinitionDto);
            case TEXT:
                return this.toEntity((SimpleFieldDefinitionDTO) fieldDefinitionDto);
            case DOCUMENT:
                // TODO document
                throw new IllegalArgumentException(fieldDefinitionDto.getType() + " not implemented!");
            default:
                throw new IllegalArgumentException(fieldDefinitionDto.getType() + " not recognized");
        }
    }

    @Mapping(source="possibleAssigneeUsers", target="assigneeIds", qualifiedByName = "mapUsersToIds")
    AssigneeDefinitionDTO toDto(AssigneeDefinition assigneeDefinition);

    @Mapping(source="possibleSelections", target="selections")
    SingleSelectionDefinitionDTO toDto(SingleSelectionDefinition singleSelectionDefinition);

    SimpleFieldDefinitionDTO toDto(SimpleFieldDefinition simpleFieldDefinition);

    @Named("mapUsersToIds")
    default ArrayList<String> mapUsersToIds(ArrayList<User> users) {
        return (ArrayList<String>) users.stream().map(User::getId).collect(Collectors.toList());
    }

    @Named("mapUuidToUuidString")
    default String mapUuidToUuidString(UUID uuid){
        return uuid.toString();
    }

    AssigneeDefinition toEntity(AssigneeDefinitionDTO assigneeDefinitionDto);

    SingleSelectionDefinition toEntity(SingleSelectionDefinitionDTO singleSelectionDefinitionDto);

    SimpleFieldDefinition toEntity(SimpleFieldDefinitionDTO simpleFieldDefinitionDto);

    default ArrayList<FieldDefinitionDTO> mapFieldsDefinitionToFieldDefinitionDTO(ArrayList<FieldDefinition> fieldDefinitions){
        ArrayList<FieldDefinitionDTO> fieldDefinitionDtoList = new ArrayList<FieldDefinitionDTO>();

        for(FieldDefinition fieldDefinition : fieldDefinitions){
            fieldDefinitionDtoList.add(this.toDto(fieldDefinition));
        }

        return fieldDefinitionDtoList;
    }
}

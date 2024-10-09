package com.unifi.taskflow.businessLogic.mappers;

import java.util.ArrayList;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.AssigneeDefinitionDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.FieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.SimpleFieldDefinitionDTO;
import com.unifi.taskflow.businessLogic.dtos.fieldDefinition.SingleSelectionDefinitionDTO;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.AssigneeDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.SimpleFieldDefinition;
import com.unifi.taskflow.domainModel.fieldDefinitions.SingleSelectionDefinition;

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
                return this.toDto((SimpleFieldDefinition) fieldDefinition);
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
                return this.toEntity((SimpleFieldDefinitionDTO) fieldDefinitionDto);
            default:
                throw new IllegalArgumentException(fieldDefinitionDto.getType() + " not recognized");
        }
    }

    @Mapping(source="possibleAssigneeUsers", target="assigneeIds", qualifiedByName = "mapUsersToIds")
    @Mapping(target = "organizationId", ignore = true)
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

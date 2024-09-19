package com.example.taskflow.Mappers.FieldDefinitionMappers;

import org.mapstruct.Mapper;
import org.mapstruct.SubclassExhaustiveStrategy;

import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;

@Mapper(componentModel = "spring", subclassExhaustiveStrategy = SubclassExhaustiveStrategy.RUNTIME_EXCEPTION, uses = {AssigneeDefinitionMapper.class})
public interface FieldDefinitionMapper {
    FieldDefinition fieldDefinitionDtoToFieldDefinition(FieldDefinitionDTO fieldDefinitionDto);
    FieldDefinitionDTO fieldDefinitionToFieldDefinitionDto(FieldDefinition fieldDefinition);
}

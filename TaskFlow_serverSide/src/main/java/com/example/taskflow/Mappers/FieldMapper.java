package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DTOs.Field.StringDTO;
import com.example.taskflow.DTOs.FieldDefinition.AssigneeDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.FieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SimpleFieldDefinitionDTO;
import com.example.taskflow.DTOs.FieldDefinition.SingleSelectionDefinitionDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SimpleFieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.SingleSelectionDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Number;
import com.example.taskflow.DomainModel.FieldPackage.SingleSelection;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.DomainModel.FieldPackage.Field;

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
public interface FieldMapper {

    FieldMapper INSTANCE = Mappers.getMapper(FieldMapper.class);

    default FieldDTO toDto(Field field) {
        switch (field.getType()) {

            case ASSIGNEE:
                return this.toDto((Assignee) field);
            case DATE:
                return this.toDto((Date) field);
            case NUMBER:
                return this.toDto((Number) field);
            case SINGLE_SELECTION:
                return this.toDto((SingleSelection) field);
            case TEXT:
                return this.toDto((Text) field);
            case DOCUMENT:
                // TODO
                throw new IllegalArgumentException(field.getType() + " not implemented!");
            default:
                throw new IllegalArgumentException(field.getType() + " not recognized");

        }
    };

    default Field toEntity(FieldDTO fieldDto) {

        switch (fieldDto.getType()) {

            case ASSIGNEE:
                return this.toEntity((AssigneeDTO) fieldDto);
            case DATE:
                return this.toEntity((DateDTO) fieldDto);
            case NUMBER:
                return this.toEntity((NumberDTO) fieldDto);
            case SINGLE_SELECTION:
                return this.toEntity((StringDTO) fieldDto);
            case TEXT:
                return this.toEntity((StringDTO) fieldDto);
            case DOCUMENT:
                // TODO
                throw new IllegalArgumentException(fieldDto.getType() + " not implemented!");
            default:
                throw new IllegalArgumentException(fieldDto.getType() + " not recognized");

        }
    }
}

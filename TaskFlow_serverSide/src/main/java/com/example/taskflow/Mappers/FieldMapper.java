package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DTOs.Field.StringDTO;
import com.example.taskflow.DTOs.FieldDefinition.AssigneeDefinitionDTO;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.AssigneeDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Number;
import com.example.taskflow.DomainModel.FieldPackage.SingleSelection;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import org.mapstruct.Named;
import org.mapstruct.Qualifier;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
    }

    default Field toEntity(FieldDTO fieldDto) {

        switch (fieldDto.getType()) {

            case ASSIGNEE:
                return this.toEntity((AssigneeDTO) fieldDto);
            case DATE:
                return this.toEntity((DateDTO) fieldDto);
            case NUMBER:
                return this.toEntity((NumberDTO) fieldDto);
            case SINGLE_SELECTION:
                return this.toEntitySingleSelection((StringDTO) fieldDto);
            case TEXT:
                return this.toEntityText((StringDTO) fieldDto);
            case DOCUMENT:
                // TODO
                throw new IllegalArgumentException(fieldDto.getType() + " not implemented!");
            default:
                throw new IllegalArgumentException(fieldDto.getType() + " not recognized");

        }
    }

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidToUuidString")
    @Mapping(source="users", target="valuesDto", qualifiedByName = "mapUsersToIds")
    AssigneeDTO toDto(Assignee assignee);

    @Named("mapUsersToIds")
    default ArrayList<String> mapUsersToIds(ArrayList<User> users) {
        return (ArrayList<String>) users.stream().map(User::getId).collect(Collectors.toList());
    }

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidToUuidString")
    @Mapping(source = "notification", target = "notification")
    @Mapping(source = "date", target = "valuesDto", ignore = true)//todo fix
    DateDTO toDto(Date date);

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidToUuidString")
    @Mapping(source = "number", target = "valuesDto", ignore = true)//todo fix
    NumberDTO toDto(Number number);

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidToUuidString")
    @Mapping(source = "selection", target = "valuesDto", ignore = true)//todo fix
    StringDTO toDto(SingleSelection singleSelection);

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidToUuidString")
    @Mapping(source = "text", target = "valuesDto", ignore = true)//todo fix
    StringDTO toDto(Text text);

    @Named("mapUuidToUuidString")
    default String mapUuidToUuidString(UUID uuid) {
        return uuid.toString();
    }

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidStringToUuid")
    //@Mapping(source = "valuesDto", target="users", ignore = true)
    @Mapping(source = "valuesDto", target="values", ignore = true)
    @Mapping(source = "fieldDefinitionId", target="fieldDefinition",ignore = true)
    Assignee toEntity(AssigneeDTO assigneeDto);

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidStringToUuid")
    @Mapping(source = "notification", target = "notification")
    @Mapping(source = "dateTime", target = "date")
    Date toEntity(DateDTO dateDto);

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidStringToUuid")
    @Mapping(source = "number", target = "number")
    Number toEntity(NumberDTO numberDto);

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidStringToUuid")
    @Mapping(source = "stringValue", target = "selection")
    SingleSelection toEntitySingleSelection(StringDTO singleSelectionDto);

    @Mapping(source="uuid", target="uuid", qualifiedByName = "mapUuidStringToUuid")
    @Mapping(source = "stringValue", target = "text")
    Text toEntityText(StringDTO textDto);

    @Named("mapUuidStringToUuid")
    default UUID mapUuidStringToUuid(String uuid) {
        return UUID.fromString(uuid);
    }
}

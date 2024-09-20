package com.example.taskflow.Mappers;

import com.example.taskflow.DTOs.NotificationDTO;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DTOs.Field.NumberDTO;
import com.example.taskflow.DTOs.Field.StringDTO;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Number;
import com.example.taskflow.DomainModel.FieldPackage.SingleSelection;
import com.example.taskflow.DomainModel.FieldPackage.Text;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
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

    // ------------------------------------------------------------------------------------- //

    @Mapping(source="users", target="userIds", qualifiedByName = "mapUsersToIds")
    @Mapping(source = "fieldDefinition.id", target = "fieldDefinitionId")
    AssigneeDTO toDto(Assignee assignee);

    @Named("mapUsersToIds")
    default ArrayList<String> mapUsersToIds(ArrayList<User> users) {
        return (ArrayList<String>) users.stream().map(User::getId).collect(Collectors.toList());
    }

    @Mapping(source = "notification", target = "notification", qualifiedByName = "mapNotificationToNotificationDto")
    @Mapping(source = "fieldDefinition.id", target = "fieldDefinitionId")
    DateDTO toDto(Date date);

    @Named("mapNotificationToNotificationDto")
    default NotificationDTO mapNotificationToNotificationDto(Notification notification){
        return Mappers.getMapper(NotificationMapper.class).toDto(notification);
    }

    @Mapping(source = "fieldDefinition.id", target = "fieldDefinitionId")
    NumberDTO toDto(Number number);

    @Mapping(source = "fieldDefinition.id", target = "fieldDefinitionId")
    StringDTO toDto(SingleSelection singleSelection);

    @Mapping(source = "fieldDefinition.id", target = "fieldDefinitionId")
    StringDTO toDto(Text text);

    // ------------------------------------------------------------------------------------- //

    @Mapping(source = "userIds", target="users", ignore = true)
    @Mapping(source = "fieldDefinitionId", target="fieldDefinition", ignore = true)
    Assignee toEntity(AssigneeDTO assigneeDto);

    @Mapping(source = "notification", target = "notification", ignore = true)
    @Mapping(source = "fieldDefinitionId", target="fieldDefinition", ignore = true)
    @Mapping(source = "dateTime", target = "dateTime")
    Date toEntity(DateDTO dateDto);

    @Mapping(source = "fieldDefinitionId", target="fieldDefinition",ignore = true)
    Number toEntity(NumberDTO numberDto);

    @Mapping(source = "fieldDefinitionId", target = "fieldDefinition", ignore = true)
    SingleSelection toEntitySingleSelection(StringDTO singleSelectionDto);

    @Mapping(source = "fieldDefinitionId", target = "fieldDefinition", ignore = true)
    Text toEntityText(StringDTO textDto);
}

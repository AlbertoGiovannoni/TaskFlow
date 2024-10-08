package com.unifi.taskflow.businessLogic.mappers;

import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.unifi.taskflow.businessLogic.dtos.NotificationDTO;
import com.unifi.taskflow.businessLogic.dtos.field.AssigneeDTO;
import com.unifi.taskflow.businessLogic.dtos.field.DateDTO;
import com.unifi.taskflow.businessLogic.dtos.field.DocumentDTO;
import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.dtos.field.NumberDTO;
import com.unifi.taskflow.businessLogic.dtos.field.SingleSelectionDTO;
import com.unifi.taskflow.businessLogic.dtos.field.TextDTO;
import com.unifi.taskflow.domainModel.Notification;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fields.Assignee;
import com.unifi.taskflow.domainModel.fields.Date;
import com.unifi.taskflow.domainModel.fields.Document;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.Number;
import com.unifi.taskflow.domainModel.fields.SingleSelection;
import com.unifi.taskflow.domainModel.fields.Text;


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
                return this.toDto((Document) field);
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
                return this.toEntity((SingleSelectionDTO) fieldDto);
            case TEXT:
                return this.toEntity((TextDTO) fieldDto);
            case DOCUMENT:
                return this.toEntity((DocumentDTO) fieldDto);
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
    SingleSelectionDTO toDto(SingleSelection singleSelection);

    @Mapping(source = "fieldDefinition.id", target = "fieldDefinitionId")
    TextDTO toDto(Text text);

    @Mapping(source = "fieldDefinition.id", target = "fieldDefinitionId")
    DocumentDTO toDto(Document text);

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

    @Mapping(source = "value", target = "value", ignore = true)
    @Mapping(source = "fieldDefinitionId", target = "fieldDefinition", ignore = true)
    SingleSelection toEntity(SingleSelectionDTO singleSelectionDto);

    @Mapping(source = "fieldDefinitionId", target = "fieldDefinition", ignore = true)
    Text toEntity(TextDTO textDto);

    @Mapping(source = "fieldDefinitionId", target = "fieldDefinition", ignore = true)
    @Mapping(target = "content", ignore = true)
    Document toEntity(DocumentDTO textDto);

    default ArrayList<FieldDTO> fieldToFieldDto(ArrayList<Field> fields){
        ArrayList<FieldDTO> fieldDtoList = new ArrayList<FieldDTO>();

        for(Field field : fields){
            fieldDtoList.add(this.toDto(field));
        }

        return fieldDtoList;
    }
}


package com.unifi.taskflow.businessLogic.mappers;

import java.util.ArrayList;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.unifi.taskflow.businessLogic.dtos.NotificationDTO;
import com.unifi.taskflow.domainModel.Notification;
import com.unifi.taskflow.domainModel.User;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(source = "receiverIds", target = "receivers", ignore = true)
    Notification toEntity(NotificationDTO notificationDto);

    @Mapping(source = "receivers", target = "receiverIds", qualifiedByName = "mapUserToUserIds")
    NotificationDTO toDto(Notification notification);

    @Named("mapUserToUserIds")
    default ArrayList<String> mapUserToUserIds(ArrayList<User> users){
        ArrayList<String> userIds = new ArrayList<>();
        if (users != null){
            for (User user : users){
                userIds.add(user.getId());
            }
        }
        return userIds;
    }
}

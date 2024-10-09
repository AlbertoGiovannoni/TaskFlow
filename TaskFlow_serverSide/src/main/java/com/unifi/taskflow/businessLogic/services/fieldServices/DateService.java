package com.unifi.taskflow.businessLogic.services.fieldServices;

import org.springframework.stereotype.Service;

import com.unifi.taskflow.businessLogic.dtos.NotificationDTO;
import com.unifi.taskflow.businessLogic.dtos.field.DateDTO;
import com.unifi.taskflow.businessLogic.dtos.field.FieldDTO;
import com.unifi.taskflow.businessLogic.mappers.FieldMapper;
import com.unifi.taskflow.businessLogic.mappers.NotificationMapper;
import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.FieldDefinitionDAO;
import com.unifi.taskflow.daos.NotificationDAO;
import com.unifi.taskflow.daos.UserDAO;
import com.unifi.taskflow.domainModel.EntityFactory;
import com.unifi.taskflow.domainModel.Notification;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fieldDefinitions.FieldDefinition;
import com.unifi.taskflow.domainModel.fields.Date;
import com.unifi.taskflow.domainModel.fields.Field;
import com.unifi.taskflow.domainModel.fields.fieldBuilders.DateBuilder;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DateService extends FieldService {
    @Autowired
    FieldDefinitionDAO fieldDefinitionDAO;
    @Autowired
    NotificationDAO notificationDao;
    @Autowired
    FieldMapper fieldMapper;
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    FieldDAO fieldDao;
    @Autowired
    UserDAO userDAO;

    @Override
    public Field pushNewField(FieldDTO fieldDto) {
        if (!(fieldDto instanceof DateDTO)) {
            throw new IllegalArgumentException(
                    "FieldDto of class " + fieldDto.getClass().getSimpleName() + " instead of DateDTO");
        }

        DateDTO dateDTO = (DateDTO) fieldDto;

        FieldDefinition fieldDefinition = fieldDefinitionDAO.findById(dateDTO.getFieldDefinitionId())
                .orElse(null);

        if (fieldDefinition == null) {
            throw new IllegalArgumentException("Wrong fieldDefinition id");
        }

        NotificationDTO notificationDto = dateDTO.getNotification();
        Notification notification = EntityFactory.getNotification();

        DateBuilder builder = (new DateBuilder(fieldDefinition))
                .addDate(dateDTO.getDateTime());

        if (notificationDto != null) {
            
            String message = notificationDto.getMessage();

            notification.setMessage(message);
            notification.setNotificationDateTime(notificationDto.getNotificationDateTime());
            notification.setReceivers(this.convertRecievers(notificationDto.getReceiverIds()));
            notification = this.notificationDao.save(notification);
            builder.addNotification(notification);
        }

        Field field = builder.build();

        field = fieldDao.save(field);

        return field;
    }

    @Override
    public Field updateField(FieldDTO fieldDto) {
        Date field = (Date) this.fieldDao.findById(fieldDto.getId()).orElse(null);

        if (field == null){
            throw new IllegalArgumentException("Field date not found");
        }

        DateDTO dateDTO = (DateDTO) fieldDto;

        if (dateDTO.getDateTime() != null) {
            field.setDateTime(dateDTO.getDateTime());
        }

        if (dateDTO.getNotification() != null) {
            NotificationDTO notificationDto = dateDTO.getNotification();
            Notification notification = this.notificationMapper.toEntity(notificationDto);
            notification.setReceivers((ArrayList<User>) this.userDAO.findAllById(notificationDto.getReceiverIds()));
            field.setNotification(notification);
        }

        this.fieldDao.save(field);

        return field;
    }

    private ArrayList<User> convertRecievers(ArrayList<String> ids) {
        ArrayList<User> receivers = new ArrayList<User>();
        User usr;

        if (ids != null && ids.size() > 0) {
            for (String id : ids) {
                usr = this.userDAO.findById(id).orElse(null);

                if (usr == null){
                    throw new IllegalArgumentException("User not found");
                }

                receivers.add(usr);
            }

        }

        return receivers;
    }
}

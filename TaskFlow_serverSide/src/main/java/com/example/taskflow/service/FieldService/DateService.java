package com.example.taskflow.service.FieldService;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.NotificationDTO;
import com.example.taskflow.DTOs.Field.AssigneeDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.EntityFactory;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.User;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldPackage.Assignee;
import com.example.taskflow.DomainModel.FieldPackage.Date;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.DateBuilder;
import com.example.taskflow.Mappers.FieldMapper;
import com.example.taskflow.Mappers.NotificationMapper;

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
            
            String message = notificationDto.getMessage() + "," + dateDTO.getDateTime();

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

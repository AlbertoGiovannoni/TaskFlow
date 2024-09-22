package com.example.taskflow.service.FieldService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.FieldDefinitionDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DAOs.UserDAO;
import com.example.taskflow.DTOs.NotificationDTO;
import com.example.taskflow.DTOs.Field.DateDTO;
import com.example.taskflow.DTOs.Field.FieldDTO;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldDefinition;
import com.example.taskflow.DomainModel.FieldDefinitionPackage.FieldType;
import com.example.taskflow.DomainModel.FieldPackage.Field;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.DateBuilder;
import com.example.taskflow.DomainModel.FieldPackage.FieldFactoryPackage.FieldBuilder;
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
    public FieldDTO createField(FieldDTO fieldDto) {
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
        Notification notification = null;

        if (notificationDto != null) {
            notification = this.notificationMapper.toEntity(notificationDto);
            notification = this.notificationDao.save(notification);
        }

        Field field = (new DateBuilder(fieldDefinition))
                .addParameters(dateDTO.getDateTime(), notification)
                .build();

        field = fieldDao.save(field);

        return fieldMapper.toDto(field);
    }
}

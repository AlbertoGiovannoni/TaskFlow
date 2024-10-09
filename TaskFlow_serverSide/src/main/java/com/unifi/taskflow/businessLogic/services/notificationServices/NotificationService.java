package com.unifi.taskflow.businessLogic.services.notificationServices;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.unifi.taskflow.daos.FieldDAO;
import com.unifi.taskflow.daos.NotificationDAO;
import com.unifi.taskflow.domainModel.Notification;
import com.unifi.taskflow.domainModel.User;
import com.unifi.taskflow.domainModel.fields.Date;

import jakarta.mail.MessagingException;

@Service
public class NotificationService {
    @Autowired
    private NotificationDAO notificationDAO;
    @Autowired
    private FieldDAO fieldDAO;
    @Autowired
    private MailService mailService;

    @Scheduled(fixedRate = 60000) // ogni minuto
    public void checkForExpiredNotifications() throws MessagingException {
        ZonedDateTime nowInRome = ZonedDateTime.now(ZoneId.of("Europe/Rome"));
        LocalDateTime now = nowInRome.toLocalDateTime();
        LocalDateTime nextMinute = now.plusMinutes(1);

        // Trova solo le notifiche che scadono nel prossimo minuto
        List<Notification> notifications = notificationDAO.findExpiringNotifications(now, nextMinute);
        System.out.println("Notification check from date: \n" + now);
        System.out.println("To date: \n" + nextMinute);

        for (Notification notification : notifications) {
            sendNotificationEmail(notification);
        }
    }

    private void sendNotificationEmail(Notification notification) throws MessagingException {
        for (User receiver : notification.getReceivers()) {

            List<Date> date = this.fieldDAO.findByNotification(notification);

            if (date == null || date.isEmpty()) {
                System.out.println("No dates found for notification: " + notification.getId());
            } else {
                System.out.println("Found dates: " + date);
            }

            this.mailService.sendEmail(receiver.getEmail(), "Notifica da TaskFlow", notification.getMessage(), date.get(0));
            
        }
    }
}

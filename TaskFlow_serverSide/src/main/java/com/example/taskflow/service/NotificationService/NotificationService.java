package com.example.taskflow.service.NotificationService;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.taskflow.DAOs.FieldDAO;
import com.example.taskflow.DAOs.NotificationDAO;
import com.example.taskflow.DomainModel.Notification;
import com.example.taskflow.DomainModel.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NotificationService {
    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private JavaMailSender mailSender;

    @Scheduled(fixedRate = 60000) // ogni minuto
    public void checkForExpiredNotifications() throws MessagingException {
        // Trova tutte le notifiche
        List<Notification> notifications = notificationDAO.findAll(); // TODO per ora le carico tutte

        LocalDateTime now = LocalDateTime.now();

        for (Notification notification : notifications) {
            if (notification.getNotificationDateTime().isBefore(now)) {
                sendNotificationEmail(notification);
                notificationDAO.delete(notification); // TODO per ora le elimino
            }
        }
    }

    private void sendNotificationEmail(Notification notification) throws MessagingException {
        for (User receiver : notification.getReceivers()) {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true per l'invio di multipart

            helper.setTo(receiver.getEmail()); // Assicurati che User abbia un campo email
            helper.setSubject("Notifica da TaskFlow");
            helper.setText(notification.getMessage(), true); // true per interpretare come HTML
            try {
                mailSender.send(message);
                System.out.println("Email inviata con successo a: " + receiver.getEmail());
            } catch (Exception e) {
                System.out.println(
                        "Errore imprevisto nell'invio dell'email a " + receiver.getEmail() + ": " + e.getMessage());
            }
        }
    }
}

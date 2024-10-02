package com.example.taskflow.service.NotificationService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.taskflow.DomainModel.FieldPackage.Date;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String receiverMail, String subject, String activityMessage, Date date) throws MessagingException {
        //String[] split = activityMessage.split(",");
        //LocalDateTime dateTime = LocalDateTime.parse(split[1]);
        // String date = dateTime.toLocalDate().toString();
        // String time = dateTime.toLocalTime().toString();
        LocalDateTime dateTime = date.getDateTime();
        String htmlContent = getTemplate(dateTime.toLocalDate().toString(), dateTime.toLocalTime().toString(), activityMessage);

        // Crea un messaggio email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(receiverMail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        try {
            mailSender.send(message);
            System.out.println("Email inviata con successo a: " + receiverMail);
        } catch (Exception e) {
            System.out.println(
                    "Errore imprevisto nell'invio dell'email a " + receiverMail + ": " + e.getMessage());
        }

    }

    private String getTemplate(String date, String time, String activityMessage) {
        return "<!DOCTYPE html>" +
                "<html lang=\"it\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <title>Notifica da TaskFlow</title>" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f4f4f4;" +
                "        }" +
                "        .container {" +
                "            width: 100%;" +
                "            max-width: 600px;" +
                "            margin: 0 auto;" +
                "            background: #ffffff;" +
                "            border-radius: 8px;" +
                "            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);" +
                "            overflow: hidden;" +
                "        }" +
                "        .header {" +
                "            background-color: #007BFF;" +
                "            padding: 20px;" +
                "            text-align: center;" +
                "            color: #000000;" + 
                "        }" +
                "        .header img {" +
                "            max-width: 100px;" +
                "            height: auto;" +
                "        }" +
                "        .content {" +
                "            padding: 20px;" +
                "        }" +
                "        .footer {" +
                "            background-color: #f4f4f4;" +
                "            text-align: center;" +
                "            padding: 10px;" +
                "            font-size: 12px;" +
                "            color: #777;" +
                "        }" +
                "        .bold {" +
                "            color: #007BFF;" +
                "            font-weight: bold;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"container\">" +
                "        <div class=\"header\">" +
                "            <img src=\"https://drive.google.com/uc?export=view&id=14Q1dI5eEw4HhPzgdkUt4-AmNG7n1uiMH\" alt=\"Logo di TaskFlow\">" +
                "            <h1>Notifica da TaskFlow</h1>" +
                "        </div>" +
                "        <div class=\"content\">" +
                "            <h2 style=\"color: #007BFF;\">Hai ricevuto una nuova notifica!</h2>" +
                "            <p>Hai ricevuto questa notifica perché hai un'attività fissata per:</p>" +
                "            <p class=\"bold\">Data: " + date + "</p>" +
                "            <p class=\"bold\">Ora: " + time + "</p>" +
                "            <p>Attività fissata:</p>" +
                "            <blockquote style=\"background: #f9f9f9; border-left: 5px solid #007BFF; padding: 10px;\">" +
                activityMessage +
                "            </blockquote>" +
                "            <p>Grazie per la tua attenzione!</p>" +
                "            <p>Il team di TaskFlow</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>&copy; 2024 TaskFlow. Tutti i diritti riservati.</p>" +
                "            <p>Per assistenza, contattaci a supporto@taskflow.com</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

}
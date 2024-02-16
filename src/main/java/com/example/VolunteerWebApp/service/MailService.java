package com.example.VolunteerWebApp.service;

import com.example.VolunteerWebApp.model.NotificationEmail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("helpother@email.com");
            mimeMessageHelper.setTo(notificationEmail.getReceiver());
            mimeMessageHelper.setSubject(notificationEmail.getTopic());
            mimeMessageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()), true);
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Email sent");
        } catch (MailException e){
            e.printStackTrace();
            throw new RuntimeException("Error, could not send email");
        }
    }

}

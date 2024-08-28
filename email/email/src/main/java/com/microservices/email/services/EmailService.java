package com.microservices.email.services;

import com.microservices.email.enums.StatusEmail;
import com.microservices.email.models.EmailModel;
import com.microservices.email.repositories.EmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    final EmailRepository emailRepository;
    final JavaMailSender javaMailSender;

    public EmailService(EmailRepository emailRepository, JavaMailSender javaMailSender){
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
    }

    @Value(value = "${spring.mail.username}")
    private String emailFrom;

    @Transactional
    public EmailModel sendMail(EmailModel emailModel){
        try {
            emailModel.setSendDateEmail(LocalDateTime.now());
            emailModel.setEmailFrom(emailFrom);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailModel.getEmailTo());
            message.setSubject(emailModel.getSubject());
            message.setText(emailModel.getText());
            javaMailSender.send(message);

            emailModel.setSendStatus(StatusEmail.SENT);
        }catch (MailException e){
            emailModel.setSendStatus(StatusEmail.ERROR);
        }finally {
            return emailRepository.save(emailModel);
        }
    }
}

package com.microservices.email.consumers;

import com.microservices.email.dtos.EmailRecordDTO;
import com.microservices.email.models.EmailModel;
import com.microservices.email.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${broker.queue.email.name}")
    public void listenEmailQueue(@Payload EmailRecordDTO emailRecordDTO){
        var emailModel = new EmailModel();
        BeanUtils.copyProperties(emailRecordDTO, emailModel);
        emailService.sendMail(emailModel);
    }
}

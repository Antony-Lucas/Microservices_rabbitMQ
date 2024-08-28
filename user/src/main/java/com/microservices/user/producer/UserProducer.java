package com.microservices.user.producer;

import com.microservices.user.dtos.EmailDTO;
import com.microservices.user.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    final RabbitTemplate rabbitTemplate;

    public UserProducer(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value(value = "${broker.queue.email.name}")
    private String routingKey;

    public void publishMessageEmail(UserModel userModel){
        var emailDTO = new EmailDTO();
        emailDTO.setUserId(userModel.getUserID());
        emailDTO.setEmailTo(userModel.getEmail());
        emailDTO.setSubject("Usuário adicionado com sucesso!");
        emailDTO.setText(userModel.getUsername() + " Seja bem vindo!, " +
                "\n Agradecemos seu cadastro :) " +
                "\n confira nossos termos de uso clicando no link abaixo para assinar nossa NewsLetter");
        rabbitTemplate.convertAndSend("", routingKey, emailDTO);
    }
}

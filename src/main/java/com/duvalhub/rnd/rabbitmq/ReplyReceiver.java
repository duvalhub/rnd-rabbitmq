package com.duvalhub.rnd.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RabbitListener(queues = AmqpConfiguration.replyQueueName)
@RequiredArgsConstructor
public class ReplyReceiver {

  @RabbitHandler
  public String handleReply(Message message, String body) {
    System.out.println("Replied <" + body + ">");
    return Optional.ofNullable(message.getMessageProperties().getReplyTo())
        .map(replyTo -> "mmm")
        .orElse(null);
  }
}

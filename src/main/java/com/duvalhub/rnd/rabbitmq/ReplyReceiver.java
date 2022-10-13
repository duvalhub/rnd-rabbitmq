package com.duvalhub.rnd.rabbitmq;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RabbitListener(bindings = @QueueBinding(value = @Queue(AmqpConfiguration.replyQueueName), exchange = @Exchange(value = AmqpConfiguration.topicExchangeName, type = ExchangeTypes.TOPIC), key = AmqpConfiguration.replyQueueName))
public class ReplyReceiver {

  @RabbitHandler
  public String handle(Message message, String body) {
    System.out.println("ReplyReceiver received  <" + body + ">");
    return Optional.ofNullable(message.getMessageProperties().getReplyTo())
        .map(replyTo -> "mmm")
        .orElse(null);
  }

}

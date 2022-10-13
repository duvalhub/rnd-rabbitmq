package com.duvalhub.rnd.rabbitmq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Address;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class Producer implements ApplicationRunner {
  private final RabbitTemplate rabbitTemplate;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Executors.newSingleThreadScheduledExecutor()
        .scheduleAtFixedRate(sender(), 0, 10, TimeUnit.SECONDS);

    Executors.newSingleThreadScheduledExecutor()
        .scheduleAtFixedRate(sendToReplyReceiver(), 5, 10, TimeUnit.SECONDS);
  }

  private Runnable sender() {
    return () -> {
      System.out.println("Sending message to Receiver...");
      rabbitTemplate.convertAndSend(AmqpConfiguration.topicExchangeName, AmqpConfiguration.routingKey, "Hello from RabbitMQ!", message -> {
        message.getMessageProperties().setReplyToAddress(new Address(AmqpConfiguration.topicExchangeName, AmqpConfiguration.replyQueueName));
        return message;
      });
    };
  }

  private Runnable sendToReplyReceiver() {
    return () -> {
      System.out.println("Sending to ReplyReceiver");
      Object response = rabbitTemplate.convertSendAndReceive(AmqpConfiguration.topicExchangeName, AmqpConfiguration.replyQueueName, "Hello ReplyConsumer");
      System.out.printf("ReplyReceiver replied %s%n", response);
    };
  }
}
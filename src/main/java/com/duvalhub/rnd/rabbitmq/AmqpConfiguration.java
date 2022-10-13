package com.duvalhub.rnd.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AmqpConfiguration {
  static final String topicExchangeName = "spring-boot-exchange";

  static final String queueName = "spring-boot";
  static final String replyQueueName = "replies";
  static final String routingKey = "foo.bar.#";

  @Bean
  Queue queue() {
    return new Queue(queueName, false);
  }

  @Bean
  Queue replyQueue() {
    return new Queue(replyQueueName, true);
  }

  @Bean
  TopicExchange exchange() {
    return new TopicExchange(topicExchangeName);
  }

  @Bean
  Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
  }

  @Bean
  Binding replyBinding(TopicExchange exchange) {
    Queue queue = replyQueue();
    return BindingBuilder.bind(queue).to(exchange).with(replyQueueName);
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                           MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(queueName);
    container.setMessageListener(listenerAdapter);
    return container;
  }

//  @Bean
//  SimpleMessageListenerContainer replyListenerContainer(ConnectionFactory connectionFactory
//      , ReplyReceiver replyReceiver) {
//    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//    container.setConnectionFactory(connectionFactory);
//    container.setQueueNames(replyQueueName);
//    container.setMessageListener(new MessageListenerAdapter(replyReceiver, "handleReply"));
//    return container;
//  }

  @Bean
  MessageListenerAdapter listenerAdapter(Receiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

}
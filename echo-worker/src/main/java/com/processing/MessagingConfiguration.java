package com.processing;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfiguration {

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean()
    Queue queue() {
        return QueueBuilder.durable("queue.processing.echo.worker")
                .withArgument("x-dead-letter-exchange", exchangeDeadLetter().getName())
                .withArgument("x-dead-letter-routing-key", bindingDeadLetter().getRoutingKey())
                .build();
    }

    @Bean
    DirectExchange exchange() {
        return ExchangeBuilder.directExchange("exchange.processing.echo").build();
    }

    @Bean
    Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("processing.echo.worker");
    }


    @Bean
    DirectExchange exchangeDeadLetter() {
        return ExchangeBuilder.directExchange("exchange.processing.x-dead-letter").build();
    }


    @Bean
    Queue queueDeadLetter() {
        return QueueBuilder.durable("queue.processing.x-dead-letter").build();
    }


    @Bean
    Binding bindingDeadLetter() {
        return BindingBuilder.bind(queueDeadLetter()).to(exchangeDeadLetter()).with("processing.x-dead-letter");
    }


}

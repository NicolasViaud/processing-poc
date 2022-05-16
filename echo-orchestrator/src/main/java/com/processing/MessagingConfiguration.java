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

    @Bean
    DirectExchange exchange() {
        return ExchangeBuilder.directExchange("exchange.processing.echo").build();
    }

    @Bean
    Queue queueStart() {
        return QueueBuilder.durable("queue.processing.echo.start").build();
    }


    @Bean
    Binding bindingStart() {
        return BindingBuilder.bind(queueStart()).to(exchange()).with("processing.echo.start");
    }

    @Bean
    Queue queueWorkerCallback() {
        return QueueBuilder.durable("queue.processing.echo.worker.callback").build();
    }


    @Bean
    Binding bindingWorkerCallback() {
        return BindingBuilder.bind(queueWorkerCallback()).to(exchange()).with("processing.echo.worker.callback");
    }

    @Bean
    Queue queueMergeCallback() {
        return QueueBuilder.durable("queue.processing.echo.merge.callback").build();
    }


    @Bean
    Binding bindingMergeCallback() {
        return BindingBuilder.bind(queueMergeCallback()).to(exchange()).with("processing.echo.merge.callback");
    }

}

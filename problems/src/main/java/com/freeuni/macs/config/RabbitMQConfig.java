package com.freeuni.macs.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    public static final String SUBMISSION_QUEUE = "submissionQueue";
    public static final String SUBMISSION_EXCHANGE = "submissionExchange";
    public static final String SUBMISSION_ROUTING_KEY = "submission.routingKey";

    public static final String RESPONSE_QUEUE = "responseQueue";
    public static final String RESPONSE_EXCHANGE = "responseExchange";
    public static final String RESPONSE_ROUTING_KEY = "response.routingKey";


    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue submissionQueue() {
        return new Queue(SUBMISSION_QUEUE, false);
    }

    @Bean
    public TopicExchange submissionExchange() {
        return new TopicExchange(SUBMISSION_EXCHANGE);
    }

    @Bean
    public Binding submissionBinding(Queue submissionQueue, TopicExchange submissionExchange) {
        return BindingBuilder.bind(submissionQueue).to(submissionExchange).with(SUBMISSION_ROUTING_KEY);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(RESPONSE_QUEUE, false);
    }

    @Bean
    public TopicExchange responseExchange() {
        return new TopicExchange(RESPONSE_EXCHANGE);
    }

    @Bean
    public Binding responseBinding(Queue responseQueue, TopicExchange responseExchange) {
        return BindingBuilder.bind(responseQueue).to(responseExchange).with(RESPONSE_ROUTING_KEY);
    }
}
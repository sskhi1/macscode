package com.freeuni.macs.service;

import com.freeuni.macs.config.RabbitMQConfig;
import com.freeuni.macs.model.SubmissionRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQProducer {

    private final AmqpTemplate amqpTemplate;

    @Autowired
    public RabbitMQProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendSubmissionRequest(SubmissionRequest submissionRequest) {
        amqpTemplate.convertAndSend(RabbitMQConfig.SUBMISSION_EXCHANGE, RabbitMQConfig.SUBMISSION_ROUTING_KEY, submissionRequest);
    }
}
package com.freeuni.macs.service;

import com.freeuni.macs.config.RabbitMQConfig;
import com.freeuni.macs.model.api.SubmitResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@Slf4j
public class RabbitMQResponseListener {

    private final BlockingQueue<List<SubmitResponse>> responseQueue = new LinkedBlockingQueue<>();

    @RabbitListener(queues = RabbitMQConfig.RESPONSE_QUEUE)
    public void receiveSubmitResponses(List<SubmitResponse> responses) {
        responseQueue.add(responses);
    }

    public List<SubmitResponse> getResponses() throws InterruptedException {
        return responseQueue.take();
    }
}
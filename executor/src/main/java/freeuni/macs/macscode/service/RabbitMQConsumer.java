package freeuni.macs.macscode.service;

import freeuni.macs.macscode.config.RabbitMQConfig;
import freeuni.macs.macscode.dto.RunCodeRequest;
import freeuni.macs.macscode.dto.SingleTestCaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RabbitMQConsumer {

    private final SubmissionService submissionService;

    @Autowired
    public RabbitMQConsumer(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @RabbitListener(queues = RabbitMQConfig.SUBMISSION_QUEUE)
    public void receiveSubmissionRequest(RunCodeRequest runCodeRequest) {
        log.info("Received submission request: {}", runCodeRequest);
        List<SingleTestCaseResult> responses = submissionService.processSubmissionRequest(runCodeRequest);
        submissionService.sendSubmitResponses(responses);
    }
}
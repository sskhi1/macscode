package freeuni.macs.macscode.service;

import freeuni.macs.macscode.config.RabbitMQConfig;
import freeuni.macs.macscode.dto.ProblemSolutionFile;
import freeuni.macs.macscode.dto.RunCodeRequest;
import freeuni.macs.macscode.dto.SingleTestCase;
import freeuni.macs.macscode.dto.SingleTestCaseResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {

    private final RabbitTemplate rabbitTemplate;
    private final CodeRunner codeRunner;

    @Autowired
    public SubmissionService(RabbitTemplate rabbitTemplate, CodeRunner codeRunner) {
        this.rabbitTemplate = rabbitTemplate;
        this.codeRunner = codeRunner;
    }

    public List<SingleTestCaseResult> processSubmissionRequest(RunCodeRequest runCodeRequest) {
        List<ProblemSolutionFile> problemSolutions = runCodeRequest.getSrcFiles();
        List<SingleTestCase> problemTestCases = runCodeRequest.getTestCases();
        return codeRunner.run(problemSolutions, problemTestCases, runCodeRequest.getProblemType());
    }

    public void sendSubmitResponses(List<SingleTestCaseResult> responses) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.RESPONSE_EXCHANGE, RabbitMQConfig.RESPONSE_ROUTING_KEY, responses);
    }
}

package com.freeuni.macs.service;

import com.freeuni.macs.exception.ProblemNotFoundException;
import com.freeuni.macs.model.*;
import com.freeuni.macs.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final TestService testService;
    private final String EXECUTION_API_URL;

    @Autowired
    public ProblemService(final ProblemRepository problemRepository,
                          final TestService testService,
                          final @Value("${execution.service.url}") String executionApiUrl) {
        this.problemRepository = problemRepository;
        this.testService = testService;
        this.EXECUTION_API_URL = executionApiUrl;
    }

    public List<Problem> getAll() {
        return problemRepository.findAll();
    }

    public Problem getProblemById(final String id) throws ProblemNotFoundException {
        Optional<Problem> problem = problemRepository.findById(id);
        String errorMessage = String.format("Problem with id %s does not exist.", id);
        return problem.orElseThrow(() -> new ProblemNotFoundException(errorMessage));
    }

    public Problem getProblem(final Long order, final Course course) throws ProblemNotFoundException {
        ProblemId problemId = new ProblemId(order, course);
        Optional<Problem> problem = problemRepository.findByProblemId(problemId);
        String errorMessage = String.format("Problem with order %d in %s course does not exist.", order, course.getValue());
        return problem.orElseThrow(() -> new ProblemNotFoundException(errorMessage));
    }

    public List<Problem> getProblemsByCourse(final Course course) {
        return problemRepository.findAllByProblemIdCourse(course);
    }

    public List<SubmitResponse> submitProblem(final SubmitRequest solution) {
        String problemId = solution.getProblemId();
        List<Test> problemTests = testService.getTestsByProblemId(problemId);

        Problem currentProblem = getProblemById(problemId);
        String mainFile = currentProblem.getMainFile();

        SubmissionRequest submissionRequest = new SubmissionRequest(
                List.of(new SolutionFile("Main.java", mainFile),
                        new SolutionFile("Solution.java", solution.getSolution())),
                problemTests);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SubmissionRequest> requestEntity = new HttpEntity<>(submissionRequest, headers);

        ParameterizedTypeReference<List<SubmitResponse>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<SubmitResponse>> responseEntity = restTemplate.exchange(String.format("%s/submission", EXECUTION_API_URL), HttpMethod.POST, requestEntity, typeRef);

        return responseEntity.getBody();

    }
}

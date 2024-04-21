package com.freeuni.macs.service;

import com.freeuni.macs.exception.ProblemNotFoundException;
import com.freeuni.macs.model.*;
import com.freeuni.macs.repository.ProblemRepository;
import org.bson.types.ObjectId;
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

    private ProblemDto convertProblemToProblemDto(Problem problem) {
        ProblemDto problemDto = new ProblemDto();
        problemDto.setId(problem.getId().toString());
        problemDto.setProblemId(problem.getProblemId());
        problemDto.setName(problem.getName());
        problemDto.setDescription(problem.getDescription());
        problemDto.setDifficulty(problem.getDifficulty());
        problemDto.setTopics(problem.getTopics());
        problemDto.setSolutionFileTemplate(problem.getSolutionFileTemplate());
        List<Test> publicTests = testService.getPublicTestsByProblemId(problem.getId());
        problemDto.setPublicTestCases(publicTests.stream()
                .map(singleTest -> TestDto.builder()
                        .input(singleTest.getInput())
                        .testNum(singleTest.getTestNum())
                        .build())
                .toList()
        );
        return problemDto;
    }

    public List<ProblemDto> getAll() {
        return problemRepository.findAll()
                .stream()
                .map(this::convertProblemToProblemDto)
                .toList();
    }

    public ProblemDto getProblemById(final String id) throws ProblemNotFoundException {
        Optional<Problem> problem = problemRepository.findById(new ObjectId(id));
        if (problem.isEmpty()) {
            String errorMessage = String.format("Problem with id %s does not exist.", id);
            throw new ProblemNotFoundException(errorMessage);
        }
        return convertProblemToProblemDto(problem.get());
    }

    public ProblemDto getProblem(final Long order, final Course course) throws ProblemNotFoundException {
        ProblemId problemId = new ProblemId(order, course);
        Optional<Problem> problem = problemRepository.findByProblemId(problemId);
        if (problem.isEmpty()) {
            String errorMessage = String.format("Problem with order %d in %s course does not exist.", order, course.getValue());
            throw new ProblemNotFoundException(errorMessage);
        }
        return convertProblemToProblemDto(problem.get());
    }

    public List<ProblemDto> getProblemsByCourse(final Course course) {
        return problemRepository.findAllByProblemIdCourse(course)
                .stream()
                .map(this::convertProblemToProblemDto)
                .toList();
    }

    public List<SubmitResponse> submitProblem(final SubmitRequest solution) {
        ObjectId problemId = new ObjectId(solution.getProblemId());
        List<Test> problemTests = testService.getTestsByProblemId(problemId);

        Optional<Problem> problem = problemRepository.findById(problemId);
        if (problem.isEmpty()) {
            String errorMessage = String.format("Problem with id %s does not exist.", problemId);
            throw new ProblemNotFoundException(errorMessage);
        }
        Problem currentProblem = problem.get();
        String mainFile = currentProblem.getMainFile();

        SubmissionRequest submissionRequest = new SubmissionRequest(
                List.of(new SolutionFile("Main.java", mainFile),
                        new SolutionFile("Solution.java", solution.getSolution())),
                problemTests);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SubmissionRequest> requestEntity = new HttpEntity<>(submissionRequest, headers);

        ParameterizedTypeReference<List<SubmitResponse>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<SubmitResponse>> responseEntity = restTemplate.exchange(
                String.format("%s/submission", EXECUTION_API_URL),
                HttpMethod.POST,
                requestEntity,
                typeRef);

        return responseEntity.getBody();

    }
}

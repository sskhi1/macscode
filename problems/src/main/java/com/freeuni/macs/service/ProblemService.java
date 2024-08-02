package com.freeuni.macs.service;

import com.freeuni.macs.exception.ProblemNotFoundException;
import com.freeuni.macs.model.*;
import com.freeuni.macs.repository.ProblemRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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
        problemDto.setType(problem.getType());
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
        Problem problem = getProblemById(problemId);

        return runProblemOnTests(solution, problem, problemTests);
    }

    public List<SubmitResponse> runProblemOnPublicTests(final SubmitRequest solution) {
        ObjectId problemId = new ObjectId(solution.getProblemId());
        List<Test> problemPublicTests = testService.getPublicTestsByProblemId(problemId);
        Problem problem = getProblemById(problemId);

        return runProblemOnTests(solution, problem, problemPublicTests);
    }

    private List<SubmitResponse> runProblemOnTests(SubmitRequest solution, Problem problem, List<Test> problemTests) {
        SubmissionRequest submissionRequest = getSubmissionRequest(solution, problem, problemTests);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SubmissionRequest> requestEntity = new HttpEntity<>(submissionRequest, headers);

        ParameterizedTypeReference<List<SubmitResponse>> typeRef = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<SubmitResponse>> responseEntity = restTemplate.exchange(
                String.format("%s/submission", EXECUTION_API_URL),
                HttpMethod.POST,
                requestEntity,
                typeRef);

        return responseEntity.getBody();
    }

    private Problem getProblemById(final ObjectId id) throws ProblemNotFoundException {
        Optional<Problem> problem = problemRepository.findById(id);
        if (problem.isEmpty()) {
            String errorMessage = String.format("Problem with id %s does not exist.", id);
            throw new ProblemNotFoundException(errorMessage);
        }
        return problem.get();
    }

    private static SubmissionRequest getSubmissionRequest(SubmitRequest solution, Problem problem, List<Test> problemTests) {
        String mainFile = problem.getMainFile();
        String solutionFile = solution.getSolution();

        String problemType = problem.getType();

        List<SingleFile> submissionFiles = new ArrayList<>();

        switch (problemType) {
            case "JAVA" -> {
                submissionFiles.add(new SingleFile("Solution.java", solutionFile));
                submissionFiles.add(new SingleFile("Main.java", mainFile));
            }
            case "CPP" -> {
                submissionFiles.add(new SingleFile("solution.h", solutionFile));
                submissionFiles.add(new SingleFile("main.cpp", mainFile));
            }
            case "KAREL" -> submissionFiles.add(new SingleFile("Solution.java", solutionFile));
            default -> throw new IllegalStateException("Unexpected value: " + problemType);
        }

        return new SubmissionRequest(
                submissionFiles,
                problemTests,
                problemType);
    }

    private String getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: {}", authentication);

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return "User not authenticated";
    }
}

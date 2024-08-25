package com.freeuni.macs.service;

import com.freeuni.macs.exception.ProblemNotFoundException;
import com.freeuni.macs.model.*;
import com.freeuni.macs.model.api.ProblemDto;
import com.freeuni.macs.model.api.SubmitResponse;
import com.freeuni.macs.model.api.TestDto;
import com.freeuni.macs.repository.ProblemRepository;
import com.freeuni.macs.repository.UserSubmissionRepository;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final UserSubmissionRepository userSubmissionRepository;
    private final TestService testService;
    private final RabbitMQProducer rabbitMQProducer;
    private final RabbitMQResponseListener rabbitMQResponseListener;

    @Autowired
    public ProblemService(final ProblemRepository problemRepository,
                          final UserSubmissionRepository userSubmissionRepository,
                          final TestService testService,
                          final RabbitMQProducer rabbitMQProducer,
                          final RabbitMQResponseListener rabbitMQResponseListener) {
        this.problemRepository = problemRepository;
        this.testService = testService;
        this.rabbitMQProducer = rabbitMQProducer;
        this.rabbitMQResponseListener = rabbitMQResponseListener;
        this.userSubmissionRepository = userSubmissionRepository;
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
                        .expectedOutput(singleTest.getOutput())
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

        return runProblemOnTests(solution, problem, problemTests, true);
    }

    public List<SubmitResponse> runProblemOnPublicTests(final SubmitRequest solution) {
        ObjectId problemId = new ObjectId(solution.getProblemId());
        List<Test> problemPublicTests = testService.getPublicTestsByProblemId(problemId);
        Problem problem = getProblemById(problemId);

        return runProblemOnTests(solution, problem, problemPublicTests, false);
    }

    private List<SubmitResponse> runProblemOnTests(SubmitRequest solution, Problem problem, List<Test> problemTests, boolean isSubmission) {
        SubmissionRequest submissionRequest = getSubmissionRequest(solution, problem, problemTests);
        rabbitMQProducer.sendSubmissionRequest(submissionRequest);
        List<SubmitResponse> responses;
        try {
            responses = rabbitMQResponseListener.getResponses();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (isSubmission) {
            assert responses != null;
            UserSubmission submission = UserSubmission.builder()
                    .submitterUsername(getCurrentUser())
                    .problem(problem)
                    .solutionFileContent(solution.getSolution())
                    .submissionDate(new Date())
                    .result(determineSubmissionResult(responses))
                    .build();
            userSubmissionRepository.save(submission);
        }

        return responses;
    }

    private String determineSubmissionResult(List<SubmitResponse> submitResponses) {
        return submitResponses.stream()
                .map(SubmitResponse::getResult)
                .filter(result -> !"PASS".equals(result))
                .findFirst()
                .orElse("ACCEPTED");
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

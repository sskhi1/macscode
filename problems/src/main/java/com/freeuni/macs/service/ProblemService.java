package com.freeuni.macs.service;

import com.freeuni.macs.exception.ProblemNotFoundException;
import com.freeuni.macs.model.Course;
import com.freeuni.macs.model.Problem;
import com.freeuni.macs.model.ProblemId;
import com.freeuni.macs.repository.ProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProblemService {
    private final ProblemRepository problemRepository;

    @Autowired
    public ProblemService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    public List<Problem> getAll() {
        return problemRepository.findAll();
    }

    public Problem getProblem(final Long order, final Course course) throws ProblemNotFoundException {
        ProblemId problemId = new ProblemId(order, course);
        Optional<Problem> problem = problemRepository.findByProblemId(problemId);
        String errorMessage = String.format("Problem with order %d in %s course does not exist.", order, course.getValue());
        return problem.orElseThrow(() -> new ProblemNotFoundException(errorMessage));
    }

    public List<Problem> getProblemsByCourse(Course course) {
        return problemRepository.findAllByProblemIdCourse(course);
    }


    public void generateAndInsertRandomProblems(int count) {
        for (int i = 0; i < count; i++) {
            Problem problem = createRandomProblem();
            problemRepository.save(problem);
        }
    }

    private Problem createRandomProblem() {
        Problem problem = new Problem();
        problem.setName("Problem " + getRandomNumber());
        List<Course> courses = Arrays.asList(Course.MET, Course.ABS, Course.PAR);
        problem.setProblemId(new ProblemId(new Random().nextLong(100), courses.get(new Random().nextInt(2))));
        problem.setDescription("Description for Problem " + getRandomNumber());
        problem.setDifficulty(getRandomDifficulty());
        problem.setTopics(getRandomTopics());
        problem.setSolutionFile("solution_" + getRandomNumber() + ".java");
        problem.setMainFile("main_" + getRandomNumber() + ".java");
        return problem;
    }

    private int getRandomNumber() {
        return new Random().nextInt(1000);
    }

    private String getRandomDifficulty() {
        List<String> difficulties = Arrays.asList("Easy", "Medium", "Hard");
        return difficulties.get(new Random().nextInt(difficulties.size()));
    }

    private List<String> getRandomTopics() {
        List<String> topics = Arrays.asList("Array", "String", "LinkedList", "Tree", "Graph");
        List<String> randomTopics = new ArrayList<>();
        int numberOfTopics = new Random().nextInt(3) + 1; // Randomly select 1 to 3 topics
        for (int i = 0; i < numberOfTopics; i++) {
            randomTopics.add(topics.get(new Random().nextInt(topics.size())));
        }
        return randomTopics;
    }
}

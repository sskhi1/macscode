package com.freeuni.macs.service;

import com.freeuni.macs.model.Test;
import com.freeuni.macs.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    private final TestRepository testRepository;

    @Autowired
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public List<Test> getTestsByProblemId(String problemId) {
        return testRepository.findTestsByProblemId(problemId);
    }
}

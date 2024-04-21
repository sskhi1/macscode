package com.freeuni.macs.service;

import com.freeuni.macs.model.Test;
import com.freeuni.macs.repository.TestRepository;
import org.bson.types.ObjectId;
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

    public List<Test> getTestsByProblemId(ObjectId problemId) {
        return testRepository.findTestsByProblemId(problemId);
    }

    public List<Test> getPublicTestsByProblemId(ObjectId problemId) {
        return testRepository.findAllPublicTests(problemId);
    }
}

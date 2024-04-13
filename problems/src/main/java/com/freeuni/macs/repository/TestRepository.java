package com.freeuni.macs.repository;

import com.freeuni.macs.model.Test;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends MongoRepository<Test, String> {
    List<Test> findTestsByProblemId(String problemId);
}

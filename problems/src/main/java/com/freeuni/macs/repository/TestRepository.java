package com.freeuni.macs.repository;

import com.freeuni.macs.model.Test;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends MongoRepository<Test, ObjectId> {
    List<Test> findTestsByProblemId(ObjectId problemId);


    @Query("{'problemId': ?0, 'isPublic': true}")
    List<Test> findAllPublicTests(ObjectId problemId);
}

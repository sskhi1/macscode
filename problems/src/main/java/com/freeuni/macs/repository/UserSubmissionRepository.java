package com.freeuni.macs.repository;

import com.freeuni.macs.model.UserSubmission;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubmissionRepository extends MongoRepository<UserSubmission, ObjectId> {

    List<UserSubmission> findAllBySubmitterUsernameOrderBySubmissionDateDesc(String username);

    List<UserSubmission> findAllByProblemIdOrderBySubmissionDateDesc(ObjectId problemId);

    List<UserSubmission> findAllBySubmitterUsernameAndProblemIdOrderBySubmissionDateDesc(String username, ObjectId problemId);
}

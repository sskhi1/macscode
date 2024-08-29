package com.freeuni.macs.repository;

import com.freeuni.macs.model.UserSubmission;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSubmissionRepository extends MongoRepository<UserSubmission, ObjectId> {

    List<UserSubmission> findAllBySubmitterUsernameOrderBySubmissionDateDesc(String username);

    List<UserSubmission> findAllByProblem_IdOrderBySubmissionDateDesc(ObjectId problemId);

    List<UserSubmission> findAllBySubmitterUsernameAndProblem_IdOrderBySubmissionDateDesc(String username, ObjectId problemId);

    Page<UserSubmission> findBySubmitterUsernameOrderBySubmissionDateDesc(String username, Pageable pageable);
}

package com.freeuni.macs.repository;

import com.freeuni.macs.model.UserSubmission;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubmissionRepository extends MongoRepository<UserSubmission, ObjectId> {
}

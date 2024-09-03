package com.freeuni.macs.repository;

import com.freeuni.macs.model.DraftProblem;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemDraftRepository extends MongoRepository<DraftProblem, ObjectId> {

}

package com.freeuni.macs.repository;

import com.freeuni.macs.model.Course;
import com.freeuni.macs.model.Problem;
import com.freeuni.macs.model.ProblemId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProblemRepository extends MongoRepository<Problem, ObjectId> {
    Optional<Problem> findByProblemId(ProblemId problemId);

    List<Problem> findAllByProblemIdCourse(Course course);
}

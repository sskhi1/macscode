package com.freeuni.macs.discussionservice.repository;

import com.freeuni.macs.discussionservice.model.Comment;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, ObjectId> {

    List<Comment> findByProblemIdOrderByCommentDateDesc(ObjectId problemId);

}

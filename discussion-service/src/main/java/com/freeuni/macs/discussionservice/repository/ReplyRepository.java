package com.freeuni.macs.discussionservice.repository;

import com.freeuni.macs.discussionservice.model.Comment;
import com.freeuni.macs.discussionservice.model.CommentReply;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends MongoRepository<CommentReply, ObjectId> {

    List<CommentReply> findByCommentIdOrderByReplyDateAsc(ObjectId problemId);

}

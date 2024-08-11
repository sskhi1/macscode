package com.freeuni.macs.discussionservice.service;

import com.freeuni.macs.discussionservice.mapper.CommentMapper;
import com.freeuni.macs.discussionservice.mapper.CommentReplyMapper;
import com.freeuni.macs.discussionservice.model.Comment;
import com.freeuni.macs.discussionservice.model.CommentReply;
import com.freeuni.macs.discussionservice.model.api.CommentDTO;
import com.freeuni.macs.discussionservice.model.api.CommentReplyDTO;
import com.freeuni.macs.discussionservice.model.api.CommentRequest;
import com.freeuni.macs.discussionservice.model.api.ReplyRequest;
import com.freeuni.macs.discussionservice.repository.CommentRepository;
import com.freeuni.macs.discussionservice.repository.ReplyRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DiscussionService {

    private final CommentRepository commentRepository;

    private final ReplyRepository replyRepository;

    @Autowired
    public DiscussionService(CommentRepository commentRepository, ReplyRepository replyRepository) {
        this.commentRepository = commentRepository;
        this.replyRepository = replyRepository;
    }


    public CommentDTO createComment(CommentRequest commentRequest) {
        Comment comment = Comment.builder()
                .problemId(new ObjectId(commentRequest.getProblemId()))
                .comment(commentRequest.getComment())
                .username(commentRequest.getUsername())
                .commentDate(new Date())
                .build();
        commentRepository.save(comment);
        return CommentMapper.toDTO(comment);
    }

    public List<CommentDTO> getComments(String problemId) {
        List<Comment> comments = commentRepository.findByProblemIdOrderByCommentDateDesc(new ObjectId(problemId));

        return CommentMapper.toDTOList(comments);
    }

    public CommentReplyDTO createReply(ReplyRequest replyRequest) {
        CommentReply reply = CommentReply.builder()
                .commentId(new ObjectId(replyRequest.getCommentId()))
                .reply(replyRequest.getReply())
                .username(replyRequest.getUsername())
                .replyDate(new Date())
                .build();
        replyRepository.save(reply);
        return CommentReplyMapper.toDTO(reply);
    }

    public List<CommentReplyDTO> getReplies(String commentId) {
        List<CommentReply> replies = replyRepository.findByCommentIdOrderByReplyDateAsc(new ObjectId(commentId));

        return CommentReplyMapper.toDTOList(replies);
    }
}

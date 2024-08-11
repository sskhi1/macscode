package com.freeuni.macs.discussionservice.mapper;

import com.freeuni.macs.discussionservice.model.CommentReply;
import com.freeuni.macs.discussionservice.model.api.CommentReplyDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentReplyMapper {

    public static CommentReplyDTO toDTO(CommentReply reply) {
        if (reply == null) {
            return null;
        }

        return CommentReplyDTO.builder()
                .id(reply.getId())
                .commentId(reply.getCommentId())
                .reply(reply.getReply())
                .username(reply.getUsername())
                .replyDate(reply.getReplyDate())
                .build();
    }

    public static CommentReply toEntity(CommentReplyDTO replyDTO) {
        if (replyDTO == null) {
            return null;
        }

        return CommentReply.builder()
                .id(replyDTO.getId())
                .commentId(replyDTO.getCommentId())
                .reply(replyDTO.getReply())
                .username(replyDTO.getUsername())
                .replyDate(replyDTO.getReplyDate())
                .build();
    }

    public static List<CommentReplyDTO> toDTOList(List<CommentReply> replies) {
        if (replies == null) {
            return null;
        }

        return replies.stream()
                .map(CommentReplyMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<CommentReply> toEntityList(List<CommentReplyDTO> replyDTOs) {
        if (replyDTOs == null) {
            return null;
        }

        return replyDTOs.stream()
                .map(CommentReplyMapper::toEntity)
                .collect(Collectors.toList());
    }
}

package com.freeuni.macs.discussionservice.mapper;

import com.freeuni.macs.discussionservice.model.Comment;
import com.freeuni.macs.discussionservice.model.api.CommentDTO;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentMapper {

    public static CommentDTO toDTO(Comment comment) {
        if (comment == null) {
            return null;
        }

        return CommentDTO.builder()
                .id(comment.getId().toString())
                .problemId(comment.getProblemId().toString())
                .comment(comment.getComment())
                .username(comment.getUsername())
                .commentDate(comment.getCommentDate())
                .build();
    }

    public static Comment toEntity(CommentDTO commentDTO) {
        if (commentDTO == null) {
            return null;
        }

        return Comment.builder()
                .id(new ObjectId(commentDTO.getId()))
                .problemId(new ObjectId(commentDTO.getProblemId()))
                .comment(commentDTO.getComment())
                .username(commentDTO.getUsername())
                .commentDate(commentDTO.getCommentDate())
                .build();
    }

    public static List<CommentDTO> toDTOList(List<Comment> comments) {
        if (comments == null) {
            return null;
        }

        return comments.stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Comment> toEntityList(List<CommentDTO> commentDTOs) {
        if (commentDTOs == null) {
            return null;
        }

        return commentDTOs.stream()
                .map(CommentMapper::toEntity)
                .collect(Collectors.toList());
    }
}

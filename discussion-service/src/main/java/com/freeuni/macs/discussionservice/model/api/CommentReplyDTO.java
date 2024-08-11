package com.freeuni.macs.discussionservice.model.api;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplyDTO {

    private ObjectId id;

    private ObjectId commentId;

    private String reply;

    private String username;

    private Date replyDate;
}

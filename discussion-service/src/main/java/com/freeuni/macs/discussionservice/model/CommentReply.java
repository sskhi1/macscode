package com.freeuni.macs.discussionservice.model;


import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Builder
@Document(collection = "replies")
public class CommentReply {

    @Id
    private ObjectId id;

    @Field(name = "comment_id")
    private ObjectId commentId;

    @Field(name = "reply")
    private String reply;

    @Field(name = "username")
    private String username;

    @Field(name = "reply_date")
    private Date replyDate;
}

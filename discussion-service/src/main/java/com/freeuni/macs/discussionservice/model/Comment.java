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
@Document(collection = "comments")
public class Comment {

    @Id
    private ObjectId id;

    @Field(name = "problem_id")
    private ObjectId problemId;

    @Field(name = "comment")
    private String comment;

    @Field(name = "username")
    private String username;

    @Field(name = "comment_date")
    private Date commentDate;
}

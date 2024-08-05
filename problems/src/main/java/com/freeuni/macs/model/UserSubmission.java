package com.freeuni.macs.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Builder
@Document(collection = "user_submissions")
public class UserSubmission {

    @Id
    private ObjectId id;

    @Field(name = "submitter_username")
    private String submitterUsername;

    @Field(name = "problem_id")
    private ObjectId problemId;

    @Field(name = "solution_file_content")
    private String solutionFileContent;

    @Field(name = "submission_date")
    private Date submissionDate;

    @Field(name = "successful")
    private String result;
}

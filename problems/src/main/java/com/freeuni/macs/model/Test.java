package com.freeuni.macs.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "tests")
public class Test {
    @Field(name = "problem_id")
    private ObjectId problemId;

    @Field(name = "test_num")
    private int testNum;

    @Field(name = "input")
    private String input;

    @Field(name = "output")
    private String output;

    @Field(name = "is_public")
    private Boolean isPublic;
}

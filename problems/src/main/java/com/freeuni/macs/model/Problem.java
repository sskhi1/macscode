package com.freeuni.macs.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "problems")
public class Problem {

    @Id
    private String id;

    @Field(name = "problem_id")
    private ProblemId problemId;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "difficulty")
    private String difficulty;

    @Field(name = "topics")
    private List<String> topics;

    @Field(name = "solution_file_template")
    private String solutionFileTemplate;

    @Field(name = "main_file")
    private String mainFile;

}
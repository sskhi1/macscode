package com.freeuni.macs.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Document(collection = "problems")
public class Problem {

    @Id
    @Field(name = "id")
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
    private List<String> topics; // TODO: Create Topic class

    @Field(name = "solution_file")
    private String solutionFile;

    @Field(name = "main_file")
    private String mainFile;

}
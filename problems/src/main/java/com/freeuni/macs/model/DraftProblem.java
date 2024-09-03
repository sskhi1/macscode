package com.freeuni.macs.model;

import com.freeuni.macs.model.api.InputOutputTestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "draft_problems")
public class DraftProblem {
    @Id
    private String id;

    private String name;
    private String description;
    private String type;
    private String difficulty;
    private List<String> topics;

    @Field("course")
    private Course course;

    @Field("solution_template_file")
    private String solutionTemplateFile;

    @Field("main_file")
    private String mainFile;

    @Field("test_cases")
    private List<InputOutputTestDto> testCases;
}

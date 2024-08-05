package com.freeuni.macs.model.api;

import com.freeuni.macs.model.ProblemId;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProblemDto {
    private String id;

    private ProblemId problemId;

    private String name;

    private String type;

    private String description;

    private String difficulty;

    private List<String> topics;

    private String solutionFileTemplate;

    private List<TestDto> publicTestCases;
}

package com.freeuni.macs.model;

import com.freeuni.macs.model.api.InputOutputTestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadProblemRequest {
    private String name;
    private String description;
    private String type;
    private String difficulty;
    private List<String> topics;


    private Course course;
    private String solutionTemplateFile;
    private String mainFile;
    private List<InputOutputTestDto> testCases;
}

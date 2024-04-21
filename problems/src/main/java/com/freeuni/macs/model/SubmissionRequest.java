package com.freeuni.macs.model;

import lombok.Data;

import java.util.List;

@Data
public class SubmissionRequest {
    private List<SolutionFile> srcFiles;
    private List<Test> testCases;
    private String type;

    public SubmissionRequest(List<SolutionFile> solutionFiles,
                             List<Test> problemTests,
                             String type) {
        this.srcFiles = solutionFiles;
        this.testCases = problemTests;
        this.type = type;
    }
}

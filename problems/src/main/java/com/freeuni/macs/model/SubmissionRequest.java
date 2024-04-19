package com.freeuni.macs.model;

import lombok.Data;

import java.util.List;

@Data
public class SubmissionRequest {
    private List<SolutionFile> srcFiles;
    private List<Test> testCases;

    public SubmissionRequest(List<SolutionFile> solutionFiles, List<Test> problemTests) {
        this.srcFiles = solutionFiles;
        this.testCases = problemTests;
    }
}

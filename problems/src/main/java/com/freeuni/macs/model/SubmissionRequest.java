package com.freeuni.macs.model;

import lombok.Data;

import java.util.List;

@Data
public class SubmissionRequest {
    private List<SingleFile> srcFiles;
    private List<Test> testCases;
    private String problemType;

    public SubmissionRequest(List<SingleFile> singleFiles,
                             List<Test> problemTests,
                             String problemType) {
        this.srcFiles = singleFiles;
        this.testCases = problemTests;
        this.problemType = problemType;
    }
}

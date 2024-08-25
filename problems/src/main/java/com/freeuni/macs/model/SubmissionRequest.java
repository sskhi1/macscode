package com.freeuni.macs.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SubmissionRequest implements Serializable {
    private static final long serialVersionUID = 1L;

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

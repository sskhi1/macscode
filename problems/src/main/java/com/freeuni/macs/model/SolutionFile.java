package com.freeuni.macs.model;

import lombok.Data;

@Data
public class SolutionFile {
    private String name;
    private String content;

    public SolutionFile(String name, String content) {
        this.name = name;
        this.content = content;
    }
}

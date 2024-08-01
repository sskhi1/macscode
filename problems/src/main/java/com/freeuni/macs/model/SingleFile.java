package com.freeuni.macs.model;

import lombok.Data;

@Data
public class SingleFile {
    private String name;
    private String content;

    public SingleFile(String name, String content) {
        this.name = name;
        this.content = content;
    }
}

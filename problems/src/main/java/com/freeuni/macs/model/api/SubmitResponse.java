package com.freeuni.macs.model.api;

import lombok.Data;

@Data
public class SubmitResponse {
    private Integer testNum;
    private String result;
    private String additionalInfo;
}

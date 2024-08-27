package com.freeuni.macs.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "Enum representing courses")
public enum Course {
    @Schema(description = "Programming Methodology - Karel")
    KAREL("Programming Methodology - Karel"),

    @Schema(description = "Programming Methodology")
    MET("Programming Methodology"),

    @Schema(description = "Programming Abstractions")
    ABS("Programming Abstractions");

    private final String value;
}

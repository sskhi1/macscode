package com.freeuni.macs.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Problem identifier")
@Data
public class ProblemId {
    private final Long order;
    private final Course course;
}

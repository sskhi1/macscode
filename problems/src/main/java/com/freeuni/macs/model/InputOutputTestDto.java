package com.freeuni.macs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputOutputTestDto {
    private int testNum;
    private boolean isPublic;
    private String input;
    private String output;
}

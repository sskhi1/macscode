package com.freeuni.macs.model.api;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class TestDto {
    private int testNum;

    private String input;

    private String expectedOutput;
}

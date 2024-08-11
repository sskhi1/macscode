package com.freeuni.macs.discussionservice.model.api;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotBlank
    private String problemId;

    @NotBlank
    private String comment;

    @NotBlank
    private String username;
}

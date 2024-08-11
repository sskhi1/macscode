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
public class ReplyRequest {

    @NotBlank
    private String commentId;

    @NotBlank
    private String reply;

    @NotBlank
    private String username;
}

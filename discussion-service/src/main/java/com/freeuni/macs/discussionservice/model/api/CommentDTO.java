package com.freeuni.macs.discussionservice.model.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private String id;

    private String problemId;

    private String comment;

    private String username;

    private Date commentDate;
}

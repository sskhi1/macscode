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
public class CommentReplyDTO {

    private String id;

    private String commentId;

    private String reply;

    private String username;

    private Date replyDate;
}

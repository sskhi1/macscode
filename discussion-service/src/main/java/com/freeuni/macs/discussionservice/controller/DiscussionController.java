package com.freeuni.macs.discussionservice.controller;


import com.freeuni.macs.discussionservice.model.api.CommentDTO;
import com.freeuni.macs.discussionservice.model.api.CommentReplyDTO;
import com.freeuni.macs.discussionservice.model.api.CommentRequest;
import com.freeuni.macs.discussionservice.model.api.ReplyRequest;
import com.freeuni.macs.discussionservice.service.DiscussionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Discussion", description = "The Discussion API")
@RestController()
@RequestMapping("${server.context-path}")
public class DiscussionController {

    private final DiscussionService discussionService;

    @Autowired
    public DiscussionController(DiscussionService discussionService) {
        this.discussionService = discussionService;
    }

    @Operation(summary = "Add a comment to a problem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping("/addComment")
    public ResponseEntity<CommentDTO> addComment(@Valid @RequestBody CommentRequest commentRequest) {
        CommentDTO savedComment = discussionService.createComment(commentRequest);
        return ResponseEntity.ok(savedComment);
    }

    @Operation(summary = "Get all comments of a problem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("/comments/{problemId}")
    public ResponseEntity<List<CommentDTO>> getComments(
            @PathVariable(name = "problemId") @Parameter(description = "Problem ID") String problemId
    ) {
        List<CommentDTO> comments = discussionService.getComments(problemId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Add a reply to a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping("/addReply")
    public ResponseEntity<CommentReplyDTO> addReply(@Valid @RequestBody ReplyRequest replyRequest) {
        CommentReplyDTO savedReply = discussionService.createReply(replyRequest);
        return ResponseEntity.ok(savedReply);
    }

    @Operation(summary = "Get all replies of a comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping("/replies/{commentId}")
    public ResponseEntity<List<CommentReplyDTO>> getReplies(
            @PathVariable(name = "commentId") @Parameter(description = "Comment ID") String commentId
    ) {
        List<CommentReplyDTO> replies = discussionService.getReplies(commentId);
        return ResponseEntity.ok(replies);
    }
}

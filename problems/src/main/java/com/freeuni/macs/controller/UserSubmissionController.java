package com.freeuni.macs.controller;

import com.freeuni.macs.model.api.UserSubmissionDto;
import com.freeuni.macs.service.UserSubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Submissions", description = "The Submissions API")
@RestController()
@RequestMapping("/submissions")
public class UserSubmissionController {
    private final UserSubmissionService userSubmissionService;

    @Autowired
    public UserSubmissionController(UserSubmissionService userSubmissionService) {
        this.userSubmissionService = userSubmissionService;
    }

    @Operation(summary = "Fetch all submissions of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping(value = "/users/{username}", produces = "application/json")
    public List<UserSubmissionDto> getUserSubmissions(
            @PathVariable(name = "username") @Parameter(description = "user name", in = ParameterIn.PATH) String username
    ) {
        return userSubmissionService.getUserSubmissions(username);
    }

    @Operation(summary = "Fetch last 10 submissions of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping(value = "/users/{username}/last", produces = "application/json")
    public List<UserSubmissionDto> getUserLastSubmissions(
            @PathVariable(name = "username") @Parameter(description = "user name", in = ParameterIn.PATH) String username
    ) {
        return userSubmissionService.getUserLastSubmissions(username);
    }

    @Operation(summary = "Fetch all submissions of a problem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping(value = "/problems/{problemId}", produces = "application/json")
    public List<UserSubmissionDto> getProblemSubmissions(
            @PathVariable(name = "problemId") @Parameter(description = "problem id", in = ParameterIn.PATH) String problemId
    ) {
        return userSubmissionService.getProblemSubmissions(problemId);
    }

    @Operation(summary = "Fetch all submissions of a problem by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping(value = "/problem/{problemId}/{username}", produces = "application/json")
    public List<UserSubmissionDto> getProblemSubmissions(
            @PathVariable(name = "problemId") @Parameter(description = "problem id", in = ParameterIn.PATH) String problemId,
            @PathVariable(name = "username") @Parameter(description = "user name", in = ParameterIn.PATH) String username
    ) {
        return userSubmissionService.getProblemUserSubmissions(problemId, username);
    }
}

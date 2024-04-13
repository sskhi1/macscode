package com.freeuni.macs.controller;

import com.freeuni.macs.model.Course;
import com.freeuni.macs.model.Problem;
import com.freeuni.macs.model.SubmitRequest;
import com.freeuni.macs.model.SubmitResponse;
import com.freeuni.macs.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Problems", description = "The Problem API")
@RestController()
@RequestMapping("/problems")
public class ProblemController {
    private final ProblemService problemService;

    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @Operation(summary = "Fetch all problems")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping(value = "/all", produces = "application/json")
    public List<Problem> getAll() {
        return problemService.getAll();
    }

    @Operation(summary = "Fetch a problem by order and course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation"),
            @ApiResponse(responseCode = "404", description = "not found")
    })
    @GetMapping(value = "/{course}/{order}", produces = "application/json")
    public Problem getProblem(
            @PathVariable(name = "order") @Parameter(description = "Problem order", in = ParameterIn.PATH) Long order,
            @PathVariable(name = "course") @Parameter(description = "Problem Course", in = ParameterIn.PATH) Course course
    ) {
        return problemService.getProblem(order, course);
    }

    @Operation(summary = "Fetch problems by course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @GetMapping(value = "/{course}", produces = "application/json")
    public List<Problem> getProblemsByCourse(
            @PathVariable(name = "course") @Parameter(description = "Problem Course") Course course
    ) {
        return problemService.getProblemsByCourse(course);
    }

    @Operation(summary = "Submit a problem")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation")
    })
    @PostMapping(value = "/submit")
    public List<SubmitResponse> submitProblem(@RequestBody SubmitRequest submission) {
        return problemService.submitProblem(submission);
    }
}

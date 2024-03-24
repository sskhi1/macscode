package freeuni.macs.macscode.controller;

import freeuni.macs.macscode.dto.ProblemSolution;
import freeuni.macs.macscode.dto.ProblemTestCases;
import freeuni.macs.macscode.dto.RunCodeRequest;
import freeuni.macs.macscode.dto.SingleTestCaseResult;
import freeuni.macs.macscode.service.JavaCodeRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class RunnerController {

    private final JavaCodeRunner javaCodeRunner;

    @PostMapping
    public List<SingleTestCaseResult> runCode(@RequestBody RunCodeRequest runCodeRequest) {
        ProblemSolution problemSolution = runCodeRequest.getSrcFiles();
        ProblemTestCases problemTestCases = new ProblemTestCases();
        problemTestCases.setTestCases(runCodeRequest.getTestCases());

        return javaCodeRunner.run(problemSolution, problemTestCases);
    }

}

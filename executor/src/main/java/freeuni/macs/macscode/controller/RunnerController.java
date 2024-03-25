package freeuni.macs.macscode.controller;

import freeuni.macs.macscode.dto.ProblemSolution;
import freeuni.macs.macscode.dto.RunCodeRequest;
import freeuni.macs.macscode.dto.SingleTestCase;
import freeuni.macs.macscode.dto.SingleTestCaseResult;
import freeuni.macs.macscode.service.JavaCodeRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submission")
@RequiredArgsConstructor
public class RunnerController {

    private final JavaCodeRunner javaCodeRunner;

    // TODO: maybe figure out better API?
    @PostMapping
    public List<SingleTestCaseResult> runCode(@RequestBody RunCodeRequest runCodeRequest) {
        ProblemSolution problemSolution = runCodeRequest.getSrcFiles();
        List<SingleTestCase> problemTestCases = runCodeRequest.getTestCases();

        return javaCodeRunner.run(problemSolution, problemTestCases);
    }

}

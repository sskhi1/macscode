package freeuni.macs.macscode.controller;

import freeuni.macs.macscode.dto.ProblemSolutionFile;
import freeuni.macs.macscode.dto.RunCodeRequest;
import freeuni.macs.macscode.dto.SingleTestCase;
import freeuni.macs.macscode.dto.SingleTestCaseResult;
import freeuni.macs.macscode.service.CodeRunner;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submission")
@RequiredArgsConstructor
public class RunnerController {

    private final CodeRunner codeRunner;

    @PostMapping
    public List<SingleTestCaseResult> runCode(@RequestBody RunCodeRequest runCodeRequest) {
        List<ProblemSolutionFile> problemSolutions = runCodeRequest.getSrcFiles();
        List<SingleTestCase> problemTestCases = runCodeRequest.getTestCases();
        return codeRunner.run(problemSolutions, problemTestCases, runCodeRequest.getType());
    }

}

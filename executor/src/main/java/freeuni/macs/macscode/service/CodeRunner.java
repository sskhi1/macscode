package freeuni.macs.macscode.service;

import freeuni.macs.macscode.dto.ProblemSolution;
import freeuni.macs.macscode.dto.SingleTestCase;
import freeuni.macs.macscode.dto.SingleTestCaseResult;

import java.util.List;

public interface CodeRunner {

    List<SingleTestCaseResult> run(ProblemSolution problemSolution,
                                   List<SingleTestCase> problemTestCases);

}

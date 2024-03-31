package freeuni.macs.macscode.service;

import freeuni.macs.macscode.dto.ProblemSolutionFile;
import freeuni.macs.macscode.dto.SingleTestCase;
import freeuni.macs.macscode.dto.SingleTestCaseResult;

import java.util.List;

public interface CodeRunner {

    List<SingleTestCaseResult> run(List<ProblemSolutionFile> problemSolution,
                                   List<SingleTestCase> problemTestCases);

}

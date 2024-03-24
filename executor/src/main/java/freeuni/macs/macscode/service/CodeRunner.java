package freeuni.macs.macscode.service;

import freeuni.macs.macscode.dto.CodeRunResult;
import freeuni.macs.macscode.dto.ProblemSolution;
import freeuni.macs.macscode.dto.ProblemTestCases;
import freeuni.macs.macscode.dto.SingleTestCaseResult;

import java.util.List;

public interface CodeRunner {

    List<SingleTestCaseResult> run(ProblemSolution problemSolution, ProblemTestCases problemTestCases);

}

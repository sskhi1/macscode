package freeuni.macs.macscode.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RunCodeRequest {

    private ProblemSolution srcFiles;
    private List<SingleTestCase> testCases;

}

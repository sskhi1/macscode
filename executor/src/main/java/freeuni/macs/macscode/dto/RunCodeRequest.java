package freeuni.macs.macscode.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RunCodeRequest {

    private List<ProblemSolutionFile> srcFiles;
    private List<SingleTestCase> testCases;
    private String type;
}

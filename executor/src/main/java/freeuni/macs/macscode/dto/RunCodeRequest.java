package freeuni.macs.macscode.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RunCodeRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ProblemSolutionFile> srcFiles;
    private List<SingleTestCase> testCases;
    private String problemType;
}

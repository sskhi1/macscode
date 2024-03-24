package freeuni.macs.macscode.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProblemSolution {

    @Getter
    @Setter
    static public class ProblemSolutionFile {
        private String name;
        private String content;
    }

    private List<ProblemSolutionFile> files;
}

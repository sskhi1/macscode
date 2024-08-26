package freeuni.macs.macscode.service;

import freeuni.macs.macscode.dto.ProblemSolutionFile;
import freeuni.macs.macscode.dto.SingleTestCase;

import java.util.List;
import java.util.Objects;

public class CodeImportAdder {

    private static final String JAVA_IMPORTS =
            "import java.util.*;\n" +
            "import java.lang.*;\n";

    private static final String CPP_IMPORT =
            "#include <bits/stdc++.h>\n" +
            "using namespace std;\n";

    public static void addImports(String imports, List<ProblemSolutionFile> problemSolution) {
        problemSolution.forEach(problemSolutionFile -> {
            String code = problemSolutionFile.getContent();
            problemSolutionFile.setContent(imports + code);
        });
    }

    public static void addImportsToCode(List<ProblemSolutionFile> problemSolution, String type) {
        if (Objects.equals(type, "JAVA")) {
            addImports(JAVA_IMPORTS, problemSolution);
        } else if (Objects.equals(type, "CPP")) {
            addImports(CPP_IMPORT, problemSolution);
        }
    }

}

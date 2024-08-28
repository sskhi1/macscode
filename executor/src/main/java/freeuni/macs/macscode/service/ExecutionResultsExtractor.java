package freeuni.macs.macscode.service;

import freeuni.macs.macscode.dto.SingleTestCaseResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExecutionResultsExtractor {

    private static final String COMPILE_ERROR = "COMPILE_ERROR";
    private static final String KAREL = "KAREL";
    private static final String KAREL_CRASHED = "KAREL_CRASHED";
    private static final String NO_BEEPER = "NO_BEEPER";

    private String readOneLineFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        return reader.readLine();
    }

    public List<SingleTestCaseResult> getResultsFromExecutionDir(Path executionDir) {
        String prefixPath = executionDir.toFile().getAbsolutePath() + "/result";
        List<SingleTestCaseResult> allTestCaseResults = new ArrayList<>();
        try {
            int testCasesCount = Integer.parseInt(readOneLineFromFile(String.format("%s/test_count.txt", prefixPath)));
            for (int i = 0; i < testCasesCount; ++i) {
                String currentPath = String.format("%s/result_%d.txt", prefixPath, i + 1);
                String result = readOneLineFromFile(currentPath);
                if (result.contains(COMPILE_ERROR)) {
                    allTestCaseResults.add(new SingleTestCaseResult(i, result, extractCompileError(currentPath)));
                } else if (result.contains(KAREL) || result.contains(KAREL_CRASHED) || result.contains(NO_BEEPER)) {
                    allTestCaseResults.add(extractKarel(currentPath, prefixPath, i));
                } else {
                    allTestCaseResults.add(new SingleTestCaseResult(i, result));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return allTestCaseResults;
    }

    private SingleTestCaseResult extractKarel(String filePath, String resultPath, int testNum) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String result = reader.readLine(); // Consume result
        BufferedReader instructionsReader = new BufferedReader(new FileReader(resultPath + "/instructions.txt"));
        StringBuilder compileInfoBuilder = new StringBuilder();
        for (String line = instructionsReader.readLine(); line != null; line = instructionsReader.readLine()) {
            compileInfoBuilder.append(line).append(" ");
        }
        return new SingleTestCaseResult(testNum, result, compileInfoBuilder.toString());
    }

    private String extractCompileError(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        reader.readLine(); // Consume COMPILE_ERROR
        StringBuilder compileInfoBuilder = new StringBuilder();
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
            compileInfoBuilder.append(line).append("\n");
        }
        return compileInfoBuilder.toString();
    }

}

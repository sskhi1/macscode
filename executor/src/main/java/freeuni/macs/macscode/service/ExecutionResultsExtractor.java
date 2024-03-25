package freeuni.macs.macscode.service;

import freeuni.macs.macscode.dto.SingleTestCaseResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExecutionResultsExtractor {


    public List<SingleTestCaseResult> getResultsFromExecutionDir(Path executionDir) {
        String resultsFilePath = String.format("%s/result/result.txt", executionDir.toFile().getAbsolutePath());
        List<SingleTestCaseResult> allTestCaseResults = new ArrayList<>();
        try {
            BufferedReader resultsReader = new BufferedReader(new FileReader(resultsFilePath));
            int testCasesCount = Integer.parseInt(resultsReader.readLine());
            for (int i = 0; i < testCasesCount; ++i) {
                String result = resultsReader.readLine();
                allTestCaseResults.add(new SingleTestCaseResult(i, result));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return allTestCaseResults;
    }

}

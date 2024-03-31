package freeuni.macs.macscode.service;

import freeuni.macs.macscode.dto.SingleTestCaseResult;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExecutionResultsExtractor {

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
                String result = readOneLineFromFile(String.format("%s/result_%d.txt", prefixPath, i + 1));
                allTestCaseResults.add(new SingleTestCaseResult(i, result));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return allTestCaseResults;
    }

}

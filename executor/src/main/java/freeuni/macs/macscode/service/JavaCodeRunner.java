package freeuni.macs.macscode.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Bind;
import com.sun.security.auth.module.UnixSystem;
import freeuni.macs.macscode.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
// TODO: we can refactor many code into another classes that can be used in parent CodeRunner
public class JavaCodeRunner implements CodeRunner {

    private final DockerClient dockerClient;
    private final UnixSystem unixSystem;

    private Path createExecutionDir(ProblemSolution problemSolution, List<SingleTestCase> problemTestCases) {
        try {
            Path executionDir = Files.createTempDirectory("java_code_running");
            String executionDirAbsPath = executionDir.toFile().getAbsolutePath();

            // Create src dir with java code
            Path srcDir = Files.createDirectory(Path.of(executionDirAbsPath + "/src"));
            String srcDirAbsPath = srcDir.toFile().getAbsolutePath();
            for (ProblemSolution.ProblemSolutionFile file : problemSolution.getFiles()) {
                Path filePath = Files.createFile(Path.of(srcDirAbsPath + "/" + file.getName()));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath.toFile().getAbsolutePath()));
                bufferedWriter.write(file.getContent());
                bufferedWriter.close();
            }

            // Create tests dir with test cases
            Path testsDir = Files.createDirectory(Path.of(executionDirAbsPath + "/tests"));
            String testsDirAbsPath = testsDir.toFile().getAbsolutePath();
            for (int i = 0; i < problemTestCases.size(); ++i) {
                SingleTestCase test = problemTestCases.get(i);

                int testIndex = i + 1;
                // TODO: somewhat duplicated code below
                String inputAbsPath = String.format("%s/in_%d.txt", testsDirAbsPath, testIndex);
                Path inputFilePath = Files.createFile(Path.of(inputAbsPath));
                BufferedWriter inputBufferedWriter = new BufferedWriter(new FileWriter(inputFilePath.toFile().getAbsolutePath()));
                inputBufferedWriter.write(test.getIn());
                inputBufferedWriter.close();

                String outputAbsPath = String.format("%s/out_%d.txt", testsDirAbsPath, testIndex);
                Path outputFilePath = Files.createFile(Path.of(outputAbsPath));
                BufferedWriter outputBufferedWriter = new BufferedWriter(new FileWriter(outputFilePath.toFile().getAbsolutePath()));
                outputBufferedWriter.write(test.getOut());
                outputBufferedWriter.close();
            }
            return executionDir;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // TODO: currently I don't like that we pass testCount in this method and as env variable
    private void runContainer(Path executionDir, int testCount) {
        // TODO: needs refactoring
        String left = executionDir.toFile().getAbsolutePath();
        String right = "/app/execution";
        String bindStr = String.format("%s:%s", left, right);
        Bind bind = Bind.parse(bindStr);

        String userStr = String.format("%s:%s", unixSystem.getUid(), unixSystem.getGid());

        String env = String.format("test_count=%d", testCount);

        CreateContainerResponse container = dockerClient
                .createContainerCmd("java-code-runner")
                .withEnv(env)
                .withBinds(bind)
                .withUser(userStr)
                .exec();

        // TODO: maybe use resultCallBack?
        dockerClient.startContainerCmd(container.getId()).exec();
        WaitContainerResultCallback resultCallback = new WaitContainerResultCallback();
        dockerClient.waitContainerCmd(container.getId()).exec(resultCallback);
        try {
            resultCallback.awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        dockerClient.removeContainerCmd(container.getId()).exec();
    }

    // TODO: maybe create some class ExecutionResultExtractor and have
    private List<SingleTestCaseResult> getResultsFromExecutionDir(Path executionDir, int testCasesCount) {
        String resultsFilePath = String.format("%s/result/result.txt", executionDir.toFile().getAbsolutePath());
        List<SingleTestCaseResult> allTestCaseResults = new ArrayList<>();
        try {
            BufferedReader resultsReader = new BufferedReader(new FileReader(resultsFilePath));
            for (int i = 0; i < testCasesCount; ++i) {
                String result = resultsReader.readLine();
                allTestCaseResults.add(new SingleTestCaseResult(i, result));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return allTestCaseResults;
    }

    @Override
    public List<SingleTestCaseResult> run(ProblemSolution problemSolution,
                                          List<SingleTestCase> problemTestCases) {
        Path executionDir = createExecutionDir(problemSolution, problemTestCases);
        runContainer(executionDir, problemTestCases.size());
        return getResultsFromExecutionDir(executionDir, problemTestCases.size());
    }
}

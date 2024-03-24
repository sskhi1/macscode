package freeuni.macs.macscode.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
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
public class JavaCodeRunner implements CodeRunner {

    private final DockerClient dockerClient;
    private final UnixSystem unixSystem;

    private Path createExecutionDir(ProblemSolution problemSolution, ProblemTestCases problemTestCases) {
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
            for (int i = 0; i < problemTestCases.getTestCases().size(); ++i) {
                SingleTestCase test = problemTestCases.getTestCases().get(i);

                int testIndex = i + 1;
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

    private void runContainer(Path executionDir, int testCount) {
//         TODO todo_zken: create random directory
//        String left = "/home/zkenshinx/macscode/code-execution/java/example";
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

        // TODO todo_zken: remove container after finished
        dockerClient.startContainerCmd(container.getId()).exec();
        // TODO todo_zken: wait till container finishes
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private List<SingleTestCaseResult> getResultsFromExecutionDir(Path executionDir, ProblemTestCases problemTestCases) {
        String resultsFilePath = String.format("%s/result/result.txt", executionDir.toFile().getAbsolutePath());
        List<SingleTestCaseResult> allTestCaseResults = new ArrayList<>();
        try {
            BufferedReader resultsReader = new BufferedReader(new FileReader(resultsFilePath));
            int testCasesCount = problemTestCases.getTestCases().size();
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
    public List<SingleTestCaseResult> run(ProblemSolution problemSolution, ProblemTestCases problemTestCases) {
        Path executionDir = createExecutionDir(problemSolution, problemTestCases);
        runContainer(executionDir, problemTestCases.getTestCases().size());
        return getResultsFromExecutionDir(executionDir, problemTestCases);
    }
}

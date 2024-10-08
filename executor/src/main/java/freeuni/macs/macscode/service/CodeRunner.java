package freeuni.macs.macscode.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Bind;
import freeuni.macs.macscode.dto.ProblemSolutionFile;
import freeuni.macs.macscode.dto.SingleTestCase;
import freeuni.macs.macscode.dto.SingleTestCaseResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CodeRunner {

    public static final String DOCKER_EXECUTION_PATH = "/app/execution";

    private final ExecutionResultsExtractor executionResultsExtractorService;
    private final DockerClient dockerClient;
    private final PermissionsProvider permissionsProvider;

    private Path createExecutionDir(List<ProblemSolutionFile> problemSolutions,
                                    List<SingleTestCase> problemTestCases) {
        try {
            Path executionDir = Files.createTempDirectory("code_running");
            String executionDirAbsPath = executionDir.toFile().getAbsolutePath();
            log.info("Creating execution dir: {}", executionDirAbsPath);

            // Create src dir
            Path srcDir = Files.createDirectory(Path.of(executionDirAbsPath + "/src"));
            String srcDirAbsPath = srcDir.toFile().getAbsolutePath();
            for (ProblemSolutionFile codeFile : problemSolutions) {
                Path destFilePath = Files.createFile(Path.of(srcDirAbsPath + "/" + codeFile.getName()));
                writeToFile(destFilePath, codeFile.getContent());
            }

            // Create tests dir with test cases
            Path testsDir = Files.createDirectory(Path.of(executionDirAbsPath + "/tests"));
            String testsDirAbsPath = testsDir.toFile().getAbsolutePath();
            for (int i = 0; i < problemTestCases.size(); ++i) {
                SingleTestCase test = problemTestCases.get(i);
                int testIndex = i + 1;

                String inputAbsPath = String.format("%s/in_%d.txt", testsDirAbsPath, testIndex);
                Path inputFilePath = Files.createFile(Path.of(inputAbsPath));
                writeToFile(inputFilePath, test.getInput());

                String outputAbsPath = String.format("%s/out_%d.txt", testsDirAbsPath, testIndex);
                Path outputFilePath = Files.createFile(Path.of(outputAbsPath));
                writeToFile(outputFilePath, test.getOutput());
            }
            return executionDir;
        } catch (IOException e) {
            log.error("Error while creating execution dir: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // TODO: currently I don't like that we pass testCount in this method and as env variable
    private void runContainer(Path executionDir, int testCount, String type) {
        String bindStr = String.format("%s:%s", executionDir.toFile().getAbsolutePath(), DOCKER_EXECUTION_PATH);
        Bind bind = Bind.parse(bindStr);
        String userStr = String.format("%s:%s", permissionsProvider.getUid(), permissionsProvider.getGid());
        String env = String.format("test_count=%d", testCount);

        String containerName;
        if (Objects.equals(type, "JAVA")) {
            containerName ="java";
        } else if (Objects.equals(type, "CPP")) {
            containerName ="cpp";
        } else {
            containerName = "karel";
        }

        CreateContainerResponse container = dockerClient
                .createContainerCmd(String.format("%s-code-runner", containerName))
                .withEnv(env, "time_limit=1", "memory_limit=262144")
                .withBinds(bind)
                .withUser(userStr)
                .exec();

        try {
            dockerClient.startContainerCmd(container.getId()).exec();
            log.info("Started container with id: {}", container.getId());
            WaitContainerResultCallback resultCallback = new WaitContainerResultCallback();
            dockerClient.waitContainerCmd(container.getId()).exec(resultCallback);
            resultCallback.awaitCompletion();
            log.info("Finished container with id: {}", container.getId());
            dockerClient.removeContainerCmd(container.getId()).exec();
        } catch (InterruptedException e) {
            log.error("Docker container error with id: {} , error: {}", container.getId(), e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public List<SingleTestCaseResult> run(List<ProblemSolutionFile> problemSolution,
                                          List<SingleTestCase> problemTestCases,
                                          String type) {
        CodeImportAdder.addImportsToCode(problemSolution, type);
        Path executionDir = createExecutionDir(problemSolution, problemTestCases);
        runContainer(executionDir, problemTestCases.size(), type);
        return executionResultsExtractorService.getResultsFromExecutionDir(executionDir);
    }

    private void writeToFile(Path path, String content) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path.toFile().getAbsolutePath()));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            log.error("Error while writing to file: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}

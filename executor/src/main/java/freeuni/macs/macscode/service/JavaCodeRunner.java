package freeuni.macs.macscode.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Bind;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JavaCodeRunner {

    private final DockerClient dockerClient;

    public void run() {
        // TODO todo_zken: create random directory
        String left = "/home/zkenshinx/macscode/code-execution/java/example";
        String right = "/app/execution";
        String bindStr = String.format("%s:%s", left, right);
        Bind bind = Bind.parse(bindStr);

        // TODO todo_zken: change withUser
        CreateContainerResponse container = dockerClient
                .createContainerCmd("java-code-runner")
                .withEnv("test_count=2")
                .withBinds(bind)
                .withUser("1000:1000")
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();
    }
}

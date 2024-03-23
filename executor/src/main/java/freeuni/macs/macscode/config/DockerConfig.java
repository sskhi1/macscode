package freeuni.macs.macscode.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DockerConfig {

    @Bean
    public DockerClientConfig dockerClientConfig() {
          return DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .build();
    }

    @Bean
    public DockerHttpClient dockerHttpClient(DockerClientConfig dockerClientConfig) {
        return new ApacheDockerHttpClient
                .Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .sslConfig(dockerClientConfig.getSSLConfig())
                .maxConnections(100)
                .build();
    }

    @Bean
    public DockerClient dockerClient(DockerClientConfig dockerClientConfig,
                                     DockerHttpClient dockerHttpClient) {
        return DockerClientImpl.getInstance(dockerClientConfig, dockerHttpClient);
    }

}

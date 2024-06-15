package com.study.solution.Configuration;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.PingCmd;
import com.github.dockerjava.api.command.VersionCmd;
import com.github.dockerjava.api.exception.DockerException;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.VersionCmdImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class DockerConfig {
    @Value("${docker.host}")
    private String host;

    @Bean
    public DockerClient dockerClient() throws IOException {
        DockerClientConfig config =
                DefaultDockerClientConfig.createDefaultConfigBuilder()
                        .withDockerHost(host)
                        .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();

        DockerClient dockerClient = DockerClientBuilder
                .getInstance(config)
                .withDockerHttpClient(httpClient)
                .build();

        try{
            VersionCmd versionCmd = dockerClient.versionCmd();
            Version versionResponse = versionCmd.exec();
            System.out.println("Docker версия: " + versionResponse.getVersion());
        } catch (Exception e) {
            System.err.println("Ошибка при подключении к Docker: " + e.getMessage());
        } finally {
            dockerClient.close();
            ((ApacheDockerHttpClient) httpClient).close();
        }

        return dockerClient;
    }
}

package com.github.pettyfer.caas.utils;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class DockerJavaTests {

    private static final String DOCKER_MASTER_HOST = "tcp://192.168.13.57:6665";
    private static final String DOCKER_NODE1_HOST = "tcp://192.168.13.61:6665";

    private static DockerClient dockerClient;

    @BeforeAll
    public static void init() {
        DefaultDockerClientConfig clientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(DOCKER_MASTER_HOST)
                .build();
        dockerClient = DockerClientBuilder.getInstance(clientConfig).build();
    }

    @Test
    @SneakyThrows
    public void listContainers() {
        List<Container> containers = dockerClient.listContainersCmd().exec();
        log.info("there is currently {} container", containers.size());
        for (Container c : containers) {
            log.info(c.toString());
        }
    }

    @Test
    @SneakyThrows
    public void listImages() {
        List<Image> images = dockerClient.listImagesCmd().exec();
        log.info("there is currently {} container", images.size());
        for (Image i : images) {
            log.info(i.toString());
        }
    }

    @Test
    @SneakyThrows
    public void createImages() {
    }


}

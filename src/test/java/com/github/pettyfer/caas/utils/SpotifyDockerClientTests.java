package com.github.pettyfer.caas.utils;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.Container;
import com.spotify.docker.client.messages.Image;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.List;

@Slf4j
public class SpotifyDockerClientTests {

    private static final String DOCKER_MASTER_HOST = "http://192.168.13.57:6665";
    private static final String DOCKER_NODE1_HOST = "http://192.168.13.61:6665";

    private static DockerClient dockerClient;

    @BeforeAll
    public static void init() {
        dockerClient = DefaultDockerClient.builder().uri(URI.create(DOCKER_MASTER_HOST)).build();
    }

    @Test
    @SneakyThrows
    public void listContainers() {
        List<Container> containers = dockerClient.listContainers();
        log.info("there is currently {} container", containers.size());
        for (Container c : containers) {
            log.info(c.toString());
        }
    }

    @Test
    @SneakyThrows
    public void listImages() {
        List<Image> images = dockerClient.listImages();
        log.info("there is currently {} image", images.size());
        for (Image i : images) {
            log.info(i.toString());
        }
    }

    @Test
    @SneakyThrows
    public void createImages() {
    }

}

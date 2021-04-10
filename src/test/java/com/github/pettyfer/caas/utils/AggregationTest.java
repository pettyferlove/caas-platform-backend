package com.github.pettyfer.caas.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.RegistryAuth;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.maven.shared.invoker.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.util.FS;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Properties;

/**
 * 聚合测试
 */
public class AggregationTest {

    private static final String PROJECT_NAME = "auto-build-examples";

    private static final String WORKSPACE_HOME = "D:/CAAS/workspace/";

    private static final String GIT_URL = "git@gitlab.ggjs.sinobest.cn:liuyang03813/auto-build-examples.git";

    private static final String BRANCH = "master"; // 默认值master

    private static final String DOCKER_MASTER_HOST = "http://192.168.13.57:6665";

    private static final String DOCKER_REGISTRY_PATH = "192.168.13.61";

    private static final String DOCKER_REGISTRY_PROJECT = "demo";

    private static DockerClient dockerClient;

    @BeforeAll
    public static void init() {
        dockerClient = DefaultDockerClient.builder().uri(URI.create(DOCKER_MASTER_HOST)).build();
    }

    /**
     * 获取远程仓库项目并构建Docker镜像
     */
    @Test
    @SneakyThrows
    public void buildImage() {
        File projectPath = this.gitPull();
        Boolean mavenBuildResult = mavenBuild(projectPath);
        StringBuilder tagName = new StringBuilder();
        tagName.append(DOCKER_REGISTRY_PATH)
                .append("/")
                .append(DOCKER_REGISTRY_PROJECT)
                .append("/")
                .append(PROJECT_NAME)
                .append(":")
                .append(System.currentTimeMillis());
        if (mavenBuildResult) {
            RegistryAuth auth = RegistryAuth.builder()
                    .username("admin")
                    .password("Admin@123456")
                    .build();
            dockerClient.build(projectPath.toPath(), DockerClient.BuildParam.name(tagName.toString()));
            dockerClient.push(tagName.toString(), auth);
        }
    }

    @SneakyThrows
    public Boolean mavenBuild(File file) {
        Invoker invoker = new DefaultInvoker();
        invoker.setMavenHome(new File("D:/Maven"));
        InvocationRequest request = new DefaultInvocationRequest();
        File[] files = file.listFiles((dir, name) -> name.equals("pom.xml"));
        if (files == null || files.length == 0) {
            throw new RuntimeException("pom.xml not fount!");
        }
        request.setPomFile(files[0]);
        request.setGoals(Collections.singletonList("package"));
        Properties properties = new Properties();
        properties.setProperty("skipTests", "true");
        request.setProperties(properties);
        InvocationResult result = invoker.execute(request);
        return result.getExitCode() == 0;
    }

    /**
     * 拉去代码
     *
     * @return
     */
    @SneakyThrows
    public File gitPull() {
        File file = new File(WORKSPACE_HOME + PROJECT_NAME);
        // 清理目录
        if (file.exists()) {
            deleteFolder(file);
        }

        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected void configure(OpenSshConfig.Host hc, Session session) {
                session.setConfig("StrictHostKeyChecking","no");
                super.configure(hc, session);
            }

            @Override
            @SneakyThrows
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                InputStream fileInputStream = new ClassPathResource("AlexPettyfer").getInputStream();
                JSch defaultJSch = new JSch();
                defaultJSch.addIdentity("Alex Pettyfer", IOUtils.toByteArray(fileInputStream), null, null);
                return defaultJSch;

            }
        };
        TransportConfigCallback callback = transport -> {
            SshTransport sshTransport = (SshTransport) transport;
            sshTransport.setSshSessionFactory(sshSessionFactory);
        };

        Git git = Git.cloneRepository()
                .setURI(GIT_URL)
                .setBranch(BRANCH)
                .setDirectory(file)
                .setTransportConfigCallback(callback)
                .call();
        return git.getRepository().getDirectory().getParentFile();
    }

    public static void deleteFolder(File file) {
        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFolder(files[i]);
                files[i].delete();
            }
        }
    }

}

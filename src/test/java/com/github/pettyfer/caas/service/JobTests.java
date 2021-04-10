package com.github.pettyfer.caas.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSONObject;
import com.github.pettyfer.caas.framework.biz.entity.BizUserConfiguration;
import com.github.pettyfer.caas.framework.biz.service.IBizGlobalConfigurationService;
import com.github.pettyfer.caas.framework.biz.service.IBizUserConfigurationService;
import com.github.pettyfer.caas.framework.core.model.GlobalConfiguration;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.batch.Job;
import io.fabric8.kubernetes.api.model.batch.JobBuilder;
import io.fabric8.kubernetes.api.model.batch.JobStatus;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("compay")
public class JobTests {

    @Autowired
    private KubernetesClient kubernetesClient;

    @Autowired
    private IBizUserConfigurationService userConfigurationService;

    @Autowired
    private IBizGlobalConfigurationService globalConfigurationService;


    @Test
    public void createJob() throws MalformedURLException {
        BizUserConfiguration userConfiguration = userConfigurationService.getById("1376424046678577153");
        GlobalConfiguration globalConfiguration = globalConfigurationService.loadConfig();
        String buildCodePath = "/code";
        String jobName = "test-build-" + UUID.randomUUID().toString();
        String s = userConfiguration.getPrivateKey().replaceAll("\n", "\\\\n");
        Map<String, String> env = new HashMap<>();
        env.put("PRIVATE_KEY", s);
        env.put("GIT_ROOT", "gitlab.ggjs.sinobest.cn");
        env.put("REMOTE_PATH", "git@gitlab.ggjs.sinobest.cn:liuyang03813/auto-build-examples.git");
        env.put("REMOTE_BRANCH", "master");
        env.put("PROJECT_NAME", "leaf6-framework");
        env.put("BUILD_TARGET_PATH", "./target");
        env.put("BUILD_TARGET_NAME", "auto-build-examples-*.jar");
        env.put("JOB_NAME", jobName);
        env.put("FILE_ID", jobName);
        env.put("FULL", "yes");
        env.put("USER_ID", "1376424046678577153");
        env.put("REMOTE_SERVER", "192.168.51.67:8885");
        env.put("BUILD_CODE_PATH", buildCodePath);

        env.put("DOCKER_REGISTRY_USERNAME", globalConfiguration.getDockerRegistryUsername());
        env.put("DOCKER_REGISTRY_PASSWORD", globalConfiguration.getDockerRegistryPassword());
        env.put("DOCKER_REGISTRY_PATH", new URL(globalConfiguration.getDockerRegistryPath()).getHost());
        String dockerfile = "FROM pettyfer/apline-open-jre8:latest\n" +
                "RUN mkdir -p /home\n" +
                "WORKDIR /home\n" +
                "EXPOSE 8881\n" +
                "COPY ./target/auto-build-examples-*.jar app.jar\n" +
                "ENTRYPOINT java -jar -Dfile.encoding=UTF-8 -Xmn64m -Xms256m -Xmx256m app.jar";
        env.put("DOCKERFILE_CONTENT", dockerfile.replaceAll("\n", "\\\\n"));
        env.put("DOCKER_IMAGE_NAME", "192.168.13.61/admin-liuyang03813/auto-build-examples");
        env.put("DOCKER_IMAGE_TAG", "1234567890");

        env.put("NOTIFICATION_FLAG", "project");
        List<VolumeMount> volumeMounts = new ArrayList<>();
        List<Volume> volumes = new ArrayList<>();
        volumeMounts.add(new VolumeMountBuilder()
                .withName("work-dir")
                .withMountPath(buildCodePath)
                .build());


        volumeMounts.add(new VolumeMountBuilder()
                .withName("cache-dir")
                .withMountPath("/usr/cache/maven")
                .build());

        volumeMounts.add(new VolumeMountBuilder()
                .withName("docker-dir")
                .withMountPath("/var/run/docker.sock")
                .build());



        volumes.add(new VolumeBuilder()
                .withName("work-dir")
                .withEmptyDir(new EmptyDirVolumeSource())
                .build());

        volumes.add(new VolumeBuilder()
                .withName("cache-dir")
                .withHostPath(new HostPathVolumeSourceBuilder()
                        .withPath("/usr/build/cache/maven")
                        .withType("DirectoryOrCreate")
                        .build())
                .build());

        volumes.add(new VolumeBuilder()
                .withName("docker-dir")
                .withHostPath(new HostPathVolumeSourceBuilder()
                        .withPath("/var/run/docker.sock")
                        .build())
                .build());

        Job job = new JobBuilder()
                .withNewMetadata()
                .withName(jobName)
                .endMetadata()
                .withNewSpec()
                .withBackoffLimit(0)
                .withNewTemplate()
                .withNewMetadata()
                .withLabels(fetchBuildLabel(jobName))
                .endMetadata()
                .withNewSpec()
                .addNewInitContainer()
                .withName("git-pull")
                .withImage("192.168.13.61/tools/build-git-tool:1.0.0")
                .withImagePullPolicy("Always")
                .withEnv(fetchEnv(env))
                .withVolumeMounts(volumeMounts)
                .endInitContainer()
                .addNewInitContainer()
                .withName("maven-build")
                .withImage("192.168.13.61/tools/build-maven-tool:1.0.0")
                .withImagePullPolicy("Always")
                .withEnv(fetchEnv(env))
                .withVolumeMounts(volumeMounts)
                .endInitContainer()
                .addNewInitContainer()
                .withName("persistence")
                .withImage("192.168.13.61/tools/build-persistence-tool:1.0.0")
                .withImagePullPolicy("Always")
                .withEnv(fetchEnv(env))
                .withVolumeMounts(volumeMounts)
                .endInitContainer()
                .addNewInitContainer()
                .withName("docker-build")
                .withImage("192.168.13.61/tools/build-docker-tool:1.0.0")
                .withImagePullPolicy("Always")
                .withEnv(fetchEnv(env))
                .withVolumeMounts(volumeMounts)
                .endInitContainer()
                .addNewContainer()
                .withName("notification")
                .withImage("192.168.13.61/tools/build-notification-tool:1.0.0")
                .withImagePullPolicy("Always")
                .withEnv(fetchEnv(env))
                .withVolumeMounts(volumeMounts)
                .endContainer()
                .withVolumes(volumes)
                .withRestartPolicy("Never")
                .addNewImagePullSecret("registry-secret-mpfup")
                .endSpec()
                .endTemplate()
                .endSpec()
                .build();

        kubernetesClient.batch().jobs().inNamespace("auto1").createOrReplace(job).getStatus();

        JobStatus status = kubernetesClient.batch().jobs().inNamespace("auto1").withName(jobName).get().getStatus();
        System.out.println(JSONObject.toJSONString(status));

    }

    @Test
    public void watchJob() {
        JobStatus status = kubernetesClient.batch().jobs().inNamespace("auto1").withName("build-c50f639f-91bc-41f5-9729-da03c4bd5ffc").get().getStatus();
        System.out.println(JSONObject.toJSONString(status));
    }

    @Test
    public void fetchLog() {
        Pod pod = kubernetesClient.pods().inNamespace("auto1").withLabel("job-name", "test-build-0a235fad-e14a-4cb7-a52a-eb52a780bc52").list().getItems().get(0);
        List<Container> containerList = pod.getSpec().getInitContainers();
        for (Container c:containerList) {
            String log = kubernetesClient.pods().inNamespace("auto1").withName(pod.getMetadata().getName()).inContainer(c.getName()).tailingLines(2000).getLog();
            System.out.println(log);
        }
    }

    private Map<String, String> fetchBuildLabel(String value) {
        Map<String, String> label = new HashMap<>();
        label.put("build", value);
        label.put("environment", "test");
        return label;
    }

    private List<EnvVar> fetchEnv(Map<String, String> env) {
        List<EnvVar> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            list.add(new EnvVarBuilder()
                    .withName(entry.getKey())
                    .withValue(entry.getValue())
                    .build());
        }
        return list;
    }


}

package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.pettyfer.caas.framework.biz.service.IBizGlobalConfigurationService;
import com.github.pettyfer.caas.framework.core.model.GlobalConfiguration;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.ISecretService;
import com.google.common.base.Preconditions;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class SecretServiceImpl implements ISecretService {

    private final KubernetesClient kubernetesClient;

    private final IBizGlobalConfigurationService globalConfigurationService;

    public SecretServiceImpl(KubernetesClient kubernetesClient, IBizGlobalConfigurationService globalConfigurationService) {
        this.kubernetesClient = kubernetesClient;
        this.globalConfigurationService = globalConfigurationService;
    }


    @Override
    @SneakyThrows
    public String createRegistrySecret(String namespace) {
        GlobalConfiguration globalConfiguration = globalConfigurationService.loadConfig();
        Preconditions.checkNotNull(globalConfiguration.getDockerRegistryPath(), "未配置镜像仓库");
        Preconditions.checkNotNull(globalConfiguration.getDockerRegistryUsername(), "未配置镜像仓库用户名");
        Preconditions.checkNotNull(globalConfiguration.getDockerRegistryPassword(), "未配置镜像仓库用户密码");
        JSONObject json = new JSONObject();
        JSONObject auths = new JSONObject();
        JSONObject server = new JSONObject();
        server.put("username", globalConfiguration.getDockerRegistryUsername());
        server.put("password", globalConfiguration.getDockerRegistryPassword());
        server.put("auth", Base64.encode(globalConfiguration.getDockerRegistryUsername() + ":" + globalConfiguration.getDockerRegistryPassword()));
        auths.put(new URL(globalConfiguration.getDockerRegistryPath()).getHost(), server);
        json.put("auths", auths);
        String encode = Base64.encode(json.toJSONString());
        Map<String, String> data = new HashMap<>();
        data.put(".dockerconfigjson", encode);
        String name = "registry-secret-" + RandomUtil.randomString(5);
        Secret secret = new SecretBuilder()
                .withNewMetadata()
                .withName(name)
                .endMetadata()
                .withType("kubernetes.io/dockerconfigjson")
                .withData(data)
                .build();
        kubernetesClient.secrets().inNamespace(namespace).createOrReplace(secret);
        return name;
    }
}

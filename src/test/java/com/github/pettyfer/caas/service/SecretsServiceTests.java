package com.github.pettyfer.caas.service;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.ISecretService;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("compay")
public class SecretsServiceTests {

    @Autowired
    private KubernetesClient kubernetesClient;

    @Autowired
    private ISecretService secretService;


    @Test
    public void createSecrets() {
        JSONObject s = new JSONObject();
        JSONObject auths = new JSONObject();
        JSONObject server = new JSONObject();
        server.put("username", "admin");
        server.put("password", "Admin@123456");
        server.put("auth", Base64.encode("admin:Admin@123456"));
        auths.put("192.168.13.61", server);
        s.put("auths", auths);
        Map<String, String> data  = new HashMap<>();
        data.put(".dockerconfigjson", Base64.encode(s.toJSONString()));
        Secret secret = new SecretBuilder()
                .withNewMetadata()
                .withName("registry-secret")
                .endMetadata()
                .withType("kubernetes.io/dockerconfigjson")
                .withData(data)
                .build();
        kubernetesClient.secrets().inNamespace("auto1").createOrReplace(secret);
    }

    @Test
    public void buildBase64() {
        String decodeStr = Base64.decodeStr("eyJhdXRocyI6eyIxOTIuMTY4LjEzLjYxIjp7InVzZXJuYW1lIjoiYWRtaW4iLCJwYXNzd29yZCI6IkFkbWluQDEyMzQ1NiIsImVtYWlsIjoicGV0dHlmZXJsb3ZlQGxpdmUuY24iLCJhdXRoIjoiWVdSdGFXNDZRV1J0YVc1QU1USXpORFUyIn19fQ==");
        System.out.println(JSON.parseObject(decodeStr));
        JSONObject data = new JSONObject();
        JSONObject auths = new JSONObject();
        JSONObject server = new JSONObject();
        server.put("username", "admin");
        server.put("password", "Admin@123456");
        server.put("email", "pettyferlove@live.cn");
        server.put("auth", Base64.encode("admin:Admin@123456"));
        auths.put("192.168.13.61", server);
        data.put("auths", auths);
        System.out.println(data);
        System.out.println(Base64.encode(data.toJSONString()));


        secretService.createRegistrySecret("auto1");
    }

}

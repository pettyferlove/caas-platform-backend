package com.github.pettyfer.caas.service;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev-ubuntu")
public class NamespaceServiceTests {

    @Autowired
    private KubernetesClient kubernetesClient;

    @Test
    public void testContent() {
        List<Namespace> items = kubernetesClient.namespaces().list().getItems();
        for (Namespace namespace: items) {
            System.out.println(namespace);
        }
    }

}

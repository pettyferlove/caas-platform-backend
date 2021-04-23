package com.github.pettyfer.caas.service;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.INetworkService;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("compay")
public class NetworkServiceTests {

    @Autowired
    private KubernetesClient kubernetesClient;

    @Autowired
    private INetworkService networkService;

    @Test
    public void createService() {
        ServicePort servicePort = new ServicePort();
        servicePort.setName("auto-build-examples" + "-http");
        servicePort.setAppProtocol("TCP");
        servicePort.setProtocol("TCP");
        servicePort.setPort(8888);
        servicePort.setTargetPort(new IntOrString(8888));
        Service service = new ServiceBuilder()
                .withNewMetadata()
                .withName("auto-build-examples")
                .addToLabels(KubernetesConstant.GLOBAL_LABEL, "auto-build-examples")
                .addToLabels(KubernetesConstant.K8S_LABEL, "auto-build-examples")
                .endMetadata()
                .withNewSpec()
                .addToPorts(servicePort)
                .addToSelector(KubernetesConstant.GLOBAL_LABEL, "auto-build-examples")
                .addToSelector(KubernetesConstant.K8S_LABEL, "auto-build-examples")
                .withType("ClusterIP")
                .withExternalIPs("192.168.13.57")
                .endSpec()
                .build();
        kubernetesClient.services().inNamespace("auto1").createOrReplace(service);

        //kubernetesClient.services().inNamespace("auto1").withLabel(KubernetesConstant.GLOBAL_LABEL, "auto-build-examples").delete();

        ServiceList auto1 = kubernetesClient.services().inNamespace("auto1").withLabel(KubernetesConstant.GLOBAL_LABEL, "auto-build-examples").list();
        System.out.println(auto1);
        Service service1 = kubernetesClient.services().inNamespace("auto1").withName("auto-build-examples").get();
        System.out.println(service1);
    }

}

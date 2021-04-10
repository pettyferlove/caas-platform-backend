package com.github.pettyfer.caas.service;

import com.github.pettyfer.caas.framework.engine.kubernetes.service.IDeploymentService;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.ReplicaSet;
import io.fabric8.kubernetes.api.model.apps.ReplicaSetList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("compay")
public class DeploymentServiceTests {

    @Autowired
    private KubernetesClient kubernetesClient;

    @Autowired
    private IDeploymentService deploymentService;

    @Test
    public void detailDeployment() {
        Deployment deployment = kubernetesClient.apps().deployments().inNamespace("default").withName("auto-deployment-demo").get();
        ReplicaSetList list = kubernetesClient.apps().replicaSets().inNamespace("default").withLabel("k8s-app","auto-build-examples").list();
        for (ReplicaSet replicaSet:list.getItems()) {
            System.out.println(replicaSet.getMetadata().getAnnotations().get("deployment.kubernetes.io/revision"));
        }
    }

    @Test
    public void getNodeInfo() {
        NodeList nodeList = kubernetesClient.nodes().list();
        for (Node node:nodeList.getItems()) {
            String ipAddress = "";
            String hostName = "";
            String nodeName = node.getMetadata().getName();
            List<NodeAddress> addresses = node.getStatus().getAddresses();
            for (NodeAddress nodeAddress:addresses) {
                if(nodeAddress.getType().equals("InternalIP")){
                    ipAddress = nodeAddress.getAddress();
                } else {
                    hostName = nodeAddress.getAddress();
                }

            }
            System.out.println(nodeName+": ip is "+ ipAddress +", host is "+ hostName);
        }
    }

    @Test
    public void history() {
        ReplicaSetList list = kubernetesClient.apps().replicaSets().inNamespace("auto1").withLabel("k8s-app","auto-build-examples").list();
        for (ReplicaSet replicaSet:list.getItems()) {
            System.out.println(replicaSet.getMetadata().getAnnotations().get("deployment.kubernetes.io/revision"));
        }
    }

    /**
     *
     */
    @Test
    public void rollback() {
        String version = "1616556237336";
        Deployment deployment = kubernetesClient.apps().deployments().inNamespace("auto1").withName("auto-build-examples").get();
        List<ReplicaSet> replicaSets = kubernetesClient.apps().replicaSets().inNamespace("auto1").withLabel(KubernetesConstant.GLOBAL_LABEL, "auto-build-examples").list().getItems();
        Optional<ReplicaSet> optionalReplicaSet = replicaSets.stream().filter(i -> i.getMetadata().getLabels().get(KubernetesConstant.VERSION_LABEL).equals(version)).findFirst();
        if(optionalReplicaSet.isPresent()){
            deployment.getMetadata().getAnnotations().put("deployment.kubernetes.io/revision", optionalReplicaSet.get().getMetadata().getAnnotations().get("deployment.kubernetes.io/revision"));
            deployment.getMetadata().getLabels().put(KubernetesConstant.VERSION_LABEL, optionalReplicaSet.get().getMetadata().getLabels().get(KubernetesConstant.VERSION_LABEL));
            deployment.getSpec().setTemplate(optionalReplicaSet.get().getSpec().getTemplate());
            kubernetesClient.apps().deployments().inNamespace("auto1").withName("auto-build-examples").patch(deployment);
        }
    }

    @Test
    public void autoScale(){
        kubernetesClient.autoscaling().v1().horizontalPodAutoscalers().inNamespace("auto1").withName("").create();
    }

}

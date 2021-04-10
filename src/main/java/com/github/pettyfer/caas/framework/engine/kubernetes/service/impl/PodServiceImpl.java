package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.PodPageView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IPodService;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class PodServiceImpl implements IPodService {

    private final KubernetesClient kubernetesClient;

    public PodServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public Page<PodPageView> page(String namespace, String deploymentName, ListQueryParams params) {
        PodList list = kubernetesClient.pods().inNamespace(namespace).withLabel(KubernetesConstant.GLOBAL_LABEL, deploymentName).list();
        List<PodPageView> deployments = list.getItems().stream()
                .skip((params.getCurrentPage() - 1) * params.getPageSize())
                .limit(params.getPageSize())
                .map(i -> {
                    List<ContainerStatus> containerStatuses = i.getStatus().getContainerStatuses();
                    ContainerStatus containerStatus = containerStatuses.stream().filter(c -> c.getName().equals(deploymentName)).findFirst().orElseGet(ContainerStatus::new);
                    return PodPageView.builder()
                            .name(i.getMetadata().getName())
                            .nodeName(i.getSpec().getNodeName())
                            .statusPhase(i.getStatus().getPhase())
                            .ip(i.getStatus().getPodIP())
                            .restartCount(containerStatus.getRestartCount())
                            .startTime(i.getStatus().getStartTime())
                            .build();
                }).collect(Collectors.toList());
        Page<PodPageView> page = new Page<>();
        page.setRecords(deployments);
        page.setCurrent(params.getCurrentPage());
        page.setSize(params.getPageSize());
        page.setTotal(list.getItems().size());
        return page;
    }

    @Override
    public void delete(String namespace, String deploymentName) {
        kubernetesClient.pods().inNamespace(namespace).withLabel(KubernetesConstant.GLOBAL_LABEL, deploymentName).delete();
    }
}

package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NamespaceDetailView;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NamespacePageView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INamespaceService;
import com.github.pettyfer.caas.global.model.Page;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Petty
 */
@Slf4j
@Service
public class NamespaceServiceImpl implements INamespaceService {

    private final KubernetesClient kubernetesClient;

    public NamespaceServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public List<NamespacePageView> listAll() {
        NamespaceList namespaceList = kubernetesClient.namespaces().list();
        return namespaceList.getItems().stream()
                .map(i -> NamespacePageView.builder().name(i.getMetadata().getName()).build()).collect(Collectors.toList());
    }

    @Override
    public Page<NamespacePageView> page(ListQueryParams params) {
        NamespaceList list = kubernetesClient.namespaces().list();
        List<NamespacePageView> namespacePageViews = list.getItems().stream()
                .skip((params.getCurrentPage() - 1) * params.getPageSize())
                .limit(params.getPageSize())
                .map(i -> {
                    boolean isOpen = false;
                    boolean injectionKey = i.getMetadata().getLabels() != null && i.getMetadata().getLabels().containsKey("istio-injection");
                    if (injectionKey && "enable".equals(i.getMetadata().getLabels().get("istio-injection"))) {
                        isOpen = true;
                    }
                    return NamespacePageView.builder()
                            .name(i.getMetadata().getName())
                            .status(i.getStatus().getPhase())
                            .istioInjection(isOpen)
                            .creationTimestamp(i.getMetadata().getCreationTimestamp())
                            .build();
                })
                .collect(Collectors.toList());
        Page<NamespacePageView> page = new Page<NamespacePageView>();
        page.setRecords(namespacePageViews);
        page.setCurrent(params.getCurrentPage());
        page.setSize(params.getPageSize());
        page.setTotal(list.getItems().size());
        return page;
    }

    @Override
    public Boolean create(Namespace namespace) {
        try {
            kubernetesClient.namespaces().create(namespace);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Namespace创建失败");
        }
    }

    @Override
    public Boolean update(String namespaceName, Namespace namespace) {
        try {
            kubernetesClient.namespaces().withName(namespaceName).replace(namespace);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Namespace更新失败");
        }
    }

    @Override
    public Boolean delete(String namespace) {
        try {
            return kubernetesClient.namespaces().withName(namespace).delete();
        } catch (Exception e) {
            throw new RuntimeException("Namespace删除失败，请检查是否存在");
        }
    }

    @Override
    public NamespaceDetailView get(String namespace) {
        Namespace ns = kubernetesClient.namespaces().withName(namespace).get();
        /*ResourceQuota resourceQuota = kubernetesClient.resourceQuotas().withName(namespace).get();*/
        return NamespaceDetailView.builder()
                .apiVersion(ns.getApiVersion())
                .additionalProperties(ns.getAdditionalProperties())
                .metadata(ns.getMetadata())
                .spec(ns.getSpec())
                .status(ns.getStatus())
                .build();
    }
}

package com.github.pettyfer.caas.framework.engine.kubernetes.service.impl;

import com.github.pettyfer.caas.framework.engine.kubernetes.model.NodeDetailView;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.INodeService;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.NodeAddress;
import io.fabric8.kubernetes.api.model.NodeList;
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
public class NodeServiceImpl implements INodeService {

    private final KubernetesClient kubernetesClient;

    public NodeServiceImpl(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public List<NodeDetailView> list() {
        NodeList list = kubernetesClient.nodes().list();
        return list.getItems().stream().map(i -> {
            NodeDetailView.NodeDetailViewBuilder builder = NodeDetailView.builder();
            return convertType(builder, i);
        }).collect(Collectors.toList());
    }

    @Override
    public NodeDetailView get(String nodeName) {
        NodeDetailView.NodeDetailViewBuilder builder = NodeDetailView.builder();
        Node node = kubernetesClient.nodes().withName(nodeName).get();
        return convertType(builder, node);
    }

    private NodeDetailView convertType(NodeDetailView.NodeDetailViewBuilder builder, Node node) {
        List<NodeAddress> addresses = node.getStatus().getAddresses();
        addresses.forEach(address -> {
            if ("InternalIP".equals(address.getType())) {
                builder.ip(address.getAddress());
            } else if ("Hostname".equals(address.getType())) {
                builder.hostName(address.getAddress());
            }
        });
        return builder.build();
    }
}

package com.github.pettyfer.caas.global.constants;

/**
 * @author Pettyfer
 */

public enum NetworkType {
    ClusterIP("ClusterIP", "集群IP"),
    NodePort("NodePort", "节点端口"),
    LoadBalancer("LoadBalancer", "负载均衡");


    private final String value;
    private final String name;

    NetworkType(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }
}

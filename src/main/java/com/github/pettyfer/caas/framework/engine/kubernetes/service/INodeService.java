package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import com.github.pettyfer.caas.framework.engine.kubernetes.model.NodeDetailView;

import java.util.List;

/**
 * @author Pettyfer
 */
public interface INodeService {

    /**
     * 获取集群Node列表
     * @return 集合
     */
    List<NodeDetailView> list();

    /**
     * 获取节点详细信息
     * @param nodeName 节点名称
     * @return 详情
     */
    NodeDetailView get(String nodeName);

}

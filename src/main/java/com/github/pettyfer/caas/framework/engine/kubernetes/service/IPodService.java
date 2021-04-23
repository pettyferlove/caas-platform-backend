package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.PodPageView;

/**
 * @author Pettyfer
 */
public interface IPodService {

    /**
     * 分页查询Pod列表
     *
     * @param namespace      命令空间
     * @param deploymentName 应用名称（Label）
     * @param params         分页参数
     * @return 分页查询结果
     */
    Page<PodPageView> page(String namespace, String deploymentName, ListQueryParams params);

    /**
     * 删除Pod
     *
     * @param namespace      命名空间
     * @param deploymentName 应用名称（Label）
     */
    void delete(String namespace, String deploymentName);
}

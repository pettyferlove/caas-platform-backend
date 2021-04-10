package com.github.pettyfer.caas.framework.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.PodPageView;

/**
 * @author Pettyfer
 */
public interface IPodCoreService {

    Boolean delete(String namespaceId, String deploymentId);

    Page<PodPageView> page(String namespaceId, String deploymentId, ListQueryParams params);

}

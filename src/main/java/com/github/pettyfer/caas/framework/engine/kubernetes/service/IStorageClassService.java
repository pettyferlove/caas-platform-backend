package com.github.pettyfer.caas.framework.engine.kubernetes.service;

import com.github.pettyfer.caas.framework.engine.kubernetes.model.ListQueryParams;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.StorageClassView;
import com.github.pettyfer.caas.global.model.Page;

import java.util.List;

/**
 * @author Pettyfer
 */
public interface IStorageClassService {

    /**
     * 获取集群储存类资源列表
     *
     * @return 集合
     */
    List<StorageClassView> list();

    Page<StorageClassView> page(String name, ListQueryParams params);
}

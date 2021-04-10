package com.github.pettyfer.caas.framework.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.engine.kubernetes.model.NamespaceDetailView;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author Pettyfer
 */
public interface INamespaceCoreService {

    List<BizNamespace> listAll();

    IPage<BizNamespace> page(BizNamespace bizNamespace, Page<BizNamespace> page);

    NamespaceDetailView detail(String id);

    Boolean create(BizNamespace namespace);

    Boolean delete(String id);

    BizNamespace get(String id);

    Boolean update(BizNamespace namespace);
}

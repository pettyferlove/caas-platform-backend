package com.github.pettyfer.caas.framework.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizPersistentStorage;
import com.github.pettyfer.caas.framework.core.model.PersistentStorageListView;
import com.github.pettyfer.caas.framework.core.model.PersistentStorageSelectView;

import java.util.List;

/**
 * @author Pettyfer
 */
public interface IPersistentStorageCoreService {

    IPage<PersistentStorageListView> page(String namespaceId, BizPersistentStorage persistentStorage, Page<BizPersistentStorage> page);

    BizPersistentStorage get(String id);

    Boolean update(BizPersistentStorage persistentStorage);

    String create(BizPersistentStorage persistentStorage);

    List<PersistentStorageSelectView> select(String namespaceId, Integer envType);

    Boolean delete(String id);
}

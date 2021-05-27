package com.github.pettyfer.caas.framework.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizConfig;
import com.github.pettyfer.caas.framework.core.model.ConfigListView;
import com.github.pettyfer.caas.framework.core.model.ConfigSelectView;

import java.util.List;

/**
 * @author Pettyfer
 */
public interface IConfigCoreService {

    /**
     * 分页查询配置文件
     * @param config 查询参数
     * @param page 分页参数
     * @return 查询结果
     */
    IPage<ConfigListView> page(BizConfig config, Page<BizConfig> page);


    /**
     * 获取配置文件详情
     * @param id 配置ID
     * @return 配置信息
     */
    BizConfig get(String id);

    /**
     *  更新配置文件
     * @param bizConfig 配置信息
     * @return 更新结果
     */
    Boolean update(BizConfig bizConfig);

    /**
     * 添加配置文件
     * @param bizConfig 配置信息
     * @return 配置ID
     */
    String create(BizConfig bizConfig);

    /**
     * 删除配置文件
     * @param id 配置ID
     * @return 删除结果
     */
    Boolean delete(String id);

    List<ConfigSelectView> configSelect(String namespaceId, Integer envType);
}

package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizConfig;
import com.github.pettyfer.caas.framework.core.model.ConfigListView;

/**
 * <p>
 * 配置文件管理 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-09
 */
public interface IBizConfigService extends IService<BizConfig> {

    /**
     * List查找
     *
     * @param bizConfig 查询参数对象
     * @param page      Page分页对象
     * @return IPage 返回结果
     */
    IPage<ConfigListView> page(BizConfig bizConfig, Page<BizConfig> page);

    /**
     * 通过Id查询BizConfig信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizConfig get(String id);

    /**
     * 通过Id删除信息
     *
     * @param id 业务主键
     * @return Boolean
     */
    Boolean delete(String id);

    /**
     * 创建数据
     *
     * @param bizConfig 要创建的对象
     * @return Boolean
     */
    String create(BizConfig bizConfig);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizConfig 对象
     * @return Boolean
     */
    Boolean update(BizConfig bizConfig);

}

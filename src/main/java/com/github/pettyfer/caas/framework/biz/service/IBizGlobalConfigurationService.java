package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizGlobalConfiguration;
import com.github.pettyfer.caas.framework.core.model.GlobalConfiguration;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Petty
 * @since 2020-06-29
 */
public interface IBizGlobalConfigurationService extends IService<BizGlobalConfiguration> {
    /**
     * 获取当前系统的配置信息
     *
     * @return 对象
     */
    BizGlobalConfiguration get();

    /**
     * 创建数据
     *
     * @param bizGlobalConfiguration 要创建的对象
     * @return Boolean
     */
    String create(BizGlobalConfiguration bizGlobalConfiguration);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizGlobalConfiguration 对象
     * @return Boolean
     */
    Boolean update(BizGlobalConfiguration bizGlobalConfiguration);

    /**
     * 加载系统配置
     * @return GlobalConfiguration
     */
    GlobalConfiguration loadConfig();

    /**
     * 检测系统全局配置是否完善
     * @return Boolean
     */
    Boolean checkConfiguration();

}

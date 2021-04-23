package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizUserConfiguration;
import com.github.pettyfer.caas.framework.core.model.UserConfiguration;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-03-29
 */
public interface IBizUserConfigurationService extends IService<BizUserConfiguration> {

    /**
     * 通过Id查询BizUserConfiguration信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizUserConfiguration get();

    /**
     * 创建数据
     *
     * @param bizUserConfiguration 要创建的对象
     * @return Boolean
     */
    String create(BizUserConfiguration bizUserConfiguration);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizUserConfiguration 对象
     * @return Boolean
     */
    Boolean update(BizUserConfiguration bizUserConfiguration);

    /**
     * 加载当前用户配置
     *
     * @return UserConfiguration
     */
    UserConfiguration loadConfig();

    /**
     * 根据具体用户ID加载相应配置
     *
     * @param userId 用户ID
     * @return UserConfiguration
     */
    UserConfiguration loadConfig(String userId);

    /**
     * 检测用户配置是否完善
     *
     * @return Boolean
     */
    Boolean checkConfiguration();

    Boolean refresh();

}

package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Preconditions;
import com.github.pettyfer.caas.framework.biz.entity.BizGlobalConfiguration;
import com.github.pettyfer.caas.framework.biz.mapper.BizGlobalConfigurationMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizGlobalConfigurationService;
import com.github.pettyfer.caas.framework.core.model.GlobalConfiguration;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2020-06-29
 */
@Slf4j
@Service
public class BizGlobalConfigurationServiceImpl extends ServiceImpl<BizGlobalConfigurationMapper, BizGlobalConfiguration> implements IBizGlobalConfigurationService {


    @Override
    public BizGlobalConfiguration get() {
        LambdaQueryWrapper<BizGlobalConfiguration> wrapper = Wrappers.<BizGlobalConfiguration>lambdaQuery();
        return this.getOne(wrapper);
    }

    @Override
    public String create(BizGlobalConfiguration bizGlobalConfiguration) {
        try {
            bizGlobalConfiguration.setCreator(SecurityUtil.getUser().getId());
            bizGlobalConfiguration.setCreateTime(LocalDateTime.now());
            if (this.save(bizGlobalConfiguration)) {
                return bizGlobalConfiguration.getId();
            } else {
                throw new RuntimeException("新增失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("服务异常");
        }
    }

    @Override
    public Boolean update(BizGlobalConfiguration bizGlobalConfiguration) {
        try {
            bizGlobalConfiguration.setModifier(SecurityUtil.getUser().getId());
            bizGlobalConfiguration.setModifyTime(LocalDateTime.now());
            return this.updateById(bizGlobalConfiguration);
        } catch (Exception e) {
            throw new RuntimeException("服务异常");
        }
    }

    @Override
    public GlobalConfiguration loadConfig() {
        LambdaQueryWrapper<BizGlobalConfiguration> wrapper = Wrappers.<BizGlobalConfiguration>lambdaQuery();
        BizGlobalConfiguration bizGlobalConfiguration = this.getOne(wrapper);
        Optional<GlobalConfiguration> convert = Optional.ofNullable(ConverterUtil.convert(bizGlobalConfiguration, new GlobalConfiguration()));
        return convert.orElseGet(GlobalConfiguration::new);
    }

    /**
     * 检测系统全局配置是否完善
     *
     * @return Boolean
     */
    @Override
    public Boolean checkConfiguration() {
        LambdaQueryWrapper<BizGlobalConfiguration> wrapper = Wrappers.<BizGlobalConfiguration>lambdaQuery();
        BizGlobalConfiguration bizGlobalConfiguration = this.getOne(wrapper);
        if (ObjectUtil.isNull(bizGlobalConfiguration)) {
            throw new BaseRuntimeException("系统全局配置检查未通过");
        }
        Preconditions.checkNotNull(bizGlobalConfiguration.getClusterServer(), "未配置集群Server");
        Preconditions.checkNotNull(bizGlobalConfiguration.getDockerRegistryPath(), "未配置Docker镜像仓库");
        Preconditions.checkNotNull(bizGlobalConfiguration.getDockerRegistryUsername(), "未配置Docker镜像仓库用户名");
        Preconditions.checkNotNull(bizGlobalConfiguration.getDockerRegistryPassword(), "未配置Docker镜像仓库密码");
        return true;
    }

}

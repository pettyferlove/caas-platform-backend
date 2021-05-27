package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizConfig;
import com.github.pettyfer.caas.framework.biz.mapper.BizConfigMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizConfigService;
import com.github.pettyfer.caas.framework.core.model.ConfigListView;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 配置文件管理 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-09
 */
@Service
public class BizConfigServiceImpl extends ServiceImpl<BizConfigMapper, BizConfig> implements IBizConfigService {

    @Override
    public IPage<ConfigListView> page(BizConfig bizConfig, Page<BizConfig> page) {
        LambdaQueryWrapper<BizConfig> queryWrapper = Wrappers.<BizConfig>lambdaQuery();
        queryWrapper.eq(BizConfig::getNamespaceId, bizConfig.getNamespaceId());
        queryWrapper.eq(BizConfig::getDelFlag, 0);
        queryWrapper.likeRight(StrUtil.isNotEmpty(bizConfig.getConfigName()), BizConfig::getConfigName, bizConfig.getConfigName());
        return baseMapper.page(page, queryWrapper);
    }

    @Override
    public BizConfig get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizConfig bizConfig) {
        bizConfig.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizConfig.setCreateTime(LocalDateTime.now());
        if (this.save(bizConfig)) {
            return bizConfig.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizConfig bizConfig) {
        bizConfig.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizConfig.setModifyTime(LocalDateTime.now());
        return this.updateById(bizConfig);
    }

}

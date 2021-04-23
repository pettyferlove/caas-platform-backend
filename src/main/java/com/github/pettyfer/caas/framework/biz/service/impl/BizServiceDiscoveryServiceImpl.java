package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizServiceDiscovery;
import com.github.pettyfer.caas.framework.biz.mapper.BizServiceDiscoveryMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizServiceDiscoveryService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-20
 */
@Service
public class BizServiceDiscoveryServiceImpl extends ServiceImpl<BizServiceDiscoveryMapper, BizServiceDiscovery> implements IBizServiceDiscoveryService {

    @Override
    public IPage<BizServiceDiscovery> page(String namespaceId, BizServiceDiscovery bizServiceDiscovery, Page<BizServiceDiscovery> page) {
        LambdaQueryWrapper<BizServiceDiscovery> queryWrapper = Wrappers.<BizServiceDiscovery>lambdaQuery()
                .eq(BizServiceDiscovery::getNamespaceId, namespaceId)
                .ne(BizServiceDiscovery::getNetwork, "none")
                .likeRight(StrUtil.isNotEmpty(bizServiceDiscovery.getName()), BizServiceDiscovery::getName, bizServiceDiscovery.getName())
                .eq(ObjectUtil.isNotNull(bizServiceDiscovery.getEnvType()), BizServiceDiscovery::getEnvType, bizServiceDiscovery.getEnvType())
                .orderByDesc(BizServiceDiscovery::getCreateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    public BizServiceDiscovery get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizServiceDiscovery bizServiceDiscovery) {
        bizServiceDiscovery.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizServiceDiscovery.setCreateTime(LocalDateTime.now());
        if (this.save(bizServiceDiscovery)) {
            return bizServiceDiscovery.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizServiceDiscovery bizServiceDiscovery) {
        bizServiceDiscovery.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizServiceDiscovery.setModifyTime(LocalDateTime.now());
        return this.updateById(bizServiceDiscovery);
    }

}

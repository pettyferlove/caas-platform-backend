package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.mapper.BizNamespaceMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-03-22
 */
@Service
public class BizNamespaceServiceImpl extends ServiceImpl<BizNamespaceMapper, BizNamespace> implements IBizNamespaceService {

    @Override
    public IPage<BizNamespace> page(BizNamespace bizNamespace, Page<BizNamespace> page) {
        LambdaQueryWrapper<BizNamespace> queryWrapper = Wrappers.<BizNamespace>lambdaQuery().orderByDesc(BizNamespace::getCreateTime);
        queryWrapper.eq(ObjectUtil.isNotNull(bizNamespace.getEnvType()), BizNamespace::getEnvType, bizNamespace.getEnvType());
        queryWrapper.likeRight(StrUtil.isNotEmpty(bizNamespace.getName()), BizNamespace::getName, bizNamespace.getName());
        return this.page(page, queryWrapper);
    }

    @Override
    public BizNamespace get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizNamespace bizNamespace) {
        bizNamespace.setCreator(SecurityUtil.getUser().getId());
        bizNamespace.setCreateTime(LocalDateTime.now());
        if (this.save(bizNamespace)) {
            return bizNamespace.getId();
        } else {
            throw new RuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizNamespace bizNamespace) {
        bizNamespace.setModifier(SecurityUtil.getUser().getId());
        bizNamespace.setModifyTime(LocalDateTime.now());
        return this.updateById(bizNamespace);
    }

}

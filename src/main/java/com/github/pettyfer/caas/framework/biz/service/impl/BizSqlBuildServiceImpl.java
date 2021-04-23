package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuild;
import com.github.pettyfer.caas.framework.biz.mapper.BizSqlBuildMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizSqlBuildService;
import com.github.pettyfer.caas.framework.core.model.SqlBuildListView;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * SQL构建 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-01
 */
@Service
public class BizSqlBuildServiceImpl extends ServiceImpl<BizSqlBuildMapper, BizSqlBuild> implements IBizSqlBuildService {

    @Override
    public IPage<SqlBuildListView> page(BizSqlBuild bizSqlBuild, Page<BizSqlBuild> page) {
        LambdaQueryWrapper<BizSqlBuild> queryWrapper = Wrappers.<BizSqlBuild>lambdaQuery()
                .eq(BizSqlBuild::getDelFlag, false);
        queryWrapper.eq(ObjectUtil.isNotNull(bizSqlBuild.getEnvType()), BizSqlBuild::getEnvType, bizSqlBuild.getEnvType());
        queryWrapper.eq(BizSqlBuild::getNamespaceId, bizSqlBuild.getNamespaceId());
        queryWrapper.likeRight(StrUtil.isNotEmpty(bizSqlBuild.getProjectName()), BizSqlBuild::getProjectName, bizSqlBuild.getProjectName());
        return baseMapper.page(page, queryWrapper);
    }

    @Override
    public BizSqlBuild get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizSqlBuild bizSqlBuild) {
        bizSqlBuild.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizSqlBuild.setCreateTime(LocalDateTime.now());
        if (this.save(bizSqlBuild)) {
            return bizSqlBuild.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizSqlBuild bizSqlBuild) {
        bizSqlBuild.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizSqlBuild.setModifyTime(LocalDateTime.now());
        return this.updateById(bizSqlBuild);
    }

}

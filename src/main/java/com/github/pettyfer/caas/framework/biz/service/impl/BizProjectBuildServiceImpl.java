package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sinobest.caas.framework.biz.entity.BizProjectBuild;
import com.sinobest.caas.framework.biz.mapper.BizProjectBuildMapper;
import com.sinobest.caas.framework.biz.service.IBizProjectBuildService;
import com.sinobest.caas.framework.core.model.ProjectBuildListView;
import com.sinobest.caas.framework.core.model.ProjectBuildSelect;
import com.sinobest.caas.utils.ConverterUtil;
import com.sinobest.caas.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 持续部署（CD） 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2020-07-03
 */
@Slf4j
@Service
public class BizProjectBuildServiceImpl extends ServiceImpl<BizProjectBuildMapper, BizProjectBuild> implements IBizProjectBuildService {

    @Override
    public IPage<ProjectBuildListView> page(BizProjectBuild bizProjectBuild, Page<BizProjectBuild> page) {

        LambdaQueryWrapper<BizProjectBuild> queryWrapper = Wrappers.lambdaQuery(bizProjectBuild)
                .eq(BizProjectBuild::getDelFlag, false);
        queryWrapper.eq(ObjectUtil.isNotNull(bizProjectBuild.getEnvType()), BizProjectBuild::getEnvType, bizProjectBuild.getEnvType());
        queryWrapper.eq(BizProjectBuild::getNamespaceId, bizProjectBuild.getNamespaceId());
        queryWrapper.likeRight(StrUtil.isNotEmpty(bizProjectBuild.getProjectName()), BizProjectBuild::getProjectName, bizProjectBuild.getProjectName());
        queryWrapper.likeRight(StrUtil.isNotEmpty(bizProjectBuild.getProjectDescribe()), BizProjectBuild::getProjectDescribe, bizProjectBuild.getProjectDescribe());
        queryWrapper.exists(StrUtil.isNotEmpty(bizProjectBuild.getKeywords()), "select km.biz_id from biz_keyword_map km where km.keyword_id in (" + bizProjectBuild.getKeywords() + ") and t.id = km.biz_id");
        return baseMapper.queryProjectBuildList(page, queryWrapper);
    }

    @Override
    public BizProjectBuild get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizProjectBuild bizProjectBuild) {
        bizProjectBuild.setCreator(SecurityUtil.getUser().getId());
        bizProjectBuild.setCreateTime(LocalDateTime.now());
        if (this.save(bizProjectBuild)) {
            return bizProjectBuild.getId();
        } else {
            throw new RuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizProjectBuild bizProjectBuild) {
        bizProjectBuild.setModifier(SecurityUtil.getUser().getId());
        bizProjectBuild.setModifyTime(LocalDateTime.now());
        return this.updateById(bizProjectBuild);
    }

    @Override
    public List<ProjectBuildSelect> select(Integer envType, String currentId, String namespaceId) {
        LambdaQueryWrapper<BizProjectBuild> queryWrapper = Wrappers.<BizProjectBuild>lambdaQuery()
                .orderByDesc(BizProjectBuild::getCreateTime)
                .eq(BizProjectBuild::getDelFlag, 0)
                .eq(BizProjectBuild::getNamespaceId, namespaceId)
                .ne(ObjectUtil.isNotNull(currentId), BizProjectBuild::getId, currentId)
                .eq(ObjectUtil.isNotNull(envType), BizProjectBuild::getEnvType, envType)
                .eq(BizProjectBuild::getCreator, SecurityUtil.getUser().getId());
        List<BizProjectBuild> list = this.list(queryWrapper);
        Optional<List<ProjectBuildSelect>> autoBuildSelects = Optional.ofNullable(ConverterUtil.convertList(BizProjectBuild.class, ProjectBuildSelect.class, list));
        return autoBuildSelects.orElseGet(ArrayList::new);

    }

    @Override
    public Boolean updateStatus(String id, int status) {
        BizProjectBuild bizProjectBuild = new BizProjectBuild();
        bizProjectBuild.setId(id);
        bizProjectBuild.setOpenAutoBuild(status);
        bizProjectBuild.setModifier(SecurityUtil.getUser().getId());
        bizProjectBuild.setModifyTime(LocalDateTime.now());
        return this.updateById(bizProjectBuild);
    }

}

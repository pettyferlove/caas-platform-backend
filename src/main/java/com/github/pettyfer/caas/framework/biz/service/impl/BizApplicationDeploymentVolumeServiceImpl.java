package com.github.pettyfer.caas.framework.biz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentVolume;
import com.github.pettyfer.caas.framework.biz.mapper.BizApplicationDeploymentVolumeMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentVolumeService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-13
 */
@Service
public class BizApplicationDeploymentVolumeServiceImpl extends ServiceImpl<BizApplicationDeploymentVolumeMapper, BizApplicationDeploymentVolume> implements IBizApplicationDeploymentVolumeService {

    @Override
    public IPage<BizApplicationDeploymentVolume> page(BizApplicationDeploymentVolume bizApplicationDeploymentVolume, Page<BizApplicationDeploymentVolume> page) {
        return this.page(page, Wrappers.lambdaQuery(bizApplicationDeploymentVolume).orderByDesc(BizApplicationDeploymentVolume::getCreateTime));
    }

    @Override
    public BizApplicationDeploymentVolume get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizApplicationDeploymentVolume bizApplicationDeploymentVolume) {
        bizApplicationDeploymentVolume.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizApplicationDeploymentVolume.setCreateTime(LocalDateTime.now());
        if (this.save(bizApplicationDeploymentVolume)) {
            return bizApplicationDeploymentVolume.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizApplicationDeploymentVolume bizApplicationDeploymentVolume) {
        bizApplicationDeploymentVolume.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizApplicationDeploymentVolume.setModifyTime(LocalDateTime.now());
        return this.updateById(bizApplicationDeploymentVolume);
    }

}

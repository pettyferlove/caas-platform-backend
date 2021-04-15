package com.github.pettyfer.caas.framework.biz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentMount;
import com.github.pettyfer.caas.framework.biz.mapper.BizApplicationDeploymentMountMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentMountService;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentMountView;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-13
 */
@Service
public class BizApplicationDeploymentMountServiceImpl extends ServiceImpl<BizApplicationDeploymentMountMapper, BizApplicationDeploymentMount> implements IBizApplicationDeploymentMountService {

    @Override
    public IPage<BizApplicationDeploymentMount> page(BizApplicationDeploymentMount bizApplicationDeploymentMount, Page<BizApplicationDeploymentMount> page) {
        return this.page(page, Wrappers.lambdaQuery(bizApplicationDeploymentMount).orderByDesc(BizApplicationDeploymentMount::getCreateTime));
    }

    @Override
    public BizApplicationDeploymentMount get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizApplicationDeploymentMount bizApplicationDeploymentMount) {
        bizApplicationDeploymentMount.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizApplicationDeploymentMount.setCreateTime(LocalDateTime.now());
        if (this.save(bizApplicationDeploymentMount)) {
            return bizApplicationDeploymentMount.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizApplicationDeploymentMount bizApplicationDeploymentMount) {
        bizApplicationDeploymentMount.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizApplicationDeploymentMount.setModifyTime(LocalDateTime.now());
        return this.updateById(bizApplicationDeploymentMount);
    }

    @Override
    public void batchInsert(String deploymentId, List<ApplicationDeploymentMountView> deploymentMounts) {
        Optional<List<BizApplicationDeploymentMount>> volumesOptional = Optional.ofNullable(ConverterUtil.convertList(ApplicationDeploymentMountView.class, BizApplicationDeploymentMount.class, deploymentMounts));
        if (volumesOptional.isPresent()) {
            List<BizApplicationDeploymentMount> networks = volumesOptional.get().stream().peek(i -> {
                i.setDeploymentId(deploymentId);
                i.setCreator(SecurityUtil.getUser().getId());
                i.setCreateTime(LocalDateTime.now());
            }).collect(Collectors.toList());
            this.saveBatch(networks);
        }
    }

}

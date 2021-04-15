package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeployment;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentMount;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentNetwork;
import com.github.pettyfer.caas.framework.biz.mapper.BizApplicationDeploymentMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentMountService;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentNetworkService;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentService;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentDetailView;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentMountView;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentNetworkView;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 应用部署 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2020-07-28
 */
@Service
public class BizApplicationDeploymentServiceImpl extends ServiceImpl<BizApplicationDeploymentMapper, BizApplicationDeployment> implements IBizApplicationDeploymentService {


    private final IBizApplicationDeploymentNetworkService bizApplicationDeploymentNetworkService;

    private final IBizApplicationDeploymentMountService bizApplicationDeploymentMountService;

    public BizApplicationDeploymentServiceImpl(IBizApplicationDeploymentNetworkService bizApplicationDeploymentNetworkService, IBizApplicationDeploymentMountService bizApplicationDeploymentMountService) {
        this.bizApplicationDeploymentNetworkService = bizApplicationDeploymentNetworkService;
        this.bizApplicationDeploymentMountService = bizApplicationDeploymentMountService;
    }

    @Override
    public IPage<BizApplicationDeployment> page(String namespaceId, BizApplicationDeployment bizApplicationDeployment, Page<BizApplicationDeployment> page) {
        LambdaQueryWrapper<BizApplicationDeployment> queryWrapper = Wrappers.<BizApplicationDeployment>lambdaQuery()
                .eq(BizApplicationDeployment::getNamespaceId, namespaceId)
                .likeRight(StrUtil.isNotEmpty(bizApplicationDeployment.getName()), BizApplicationDeployment::getName, bizApplicationDeployment.getName())
                .eq(ObjectUtil.isNotNull(bizApplicationDeployment.getEnvType()), BizApplicationDeployment::getEnvType, bizApplicationDeployment.getEnvType())
                .orderByDesc(BizApplicationDeployment::getCreateTime);
        return this.page(page, queryWrapper);
    }

    @Override
    public ApplicationDeploymentDetailView get(String id) {
        BizApplicationDeployment applicationDeployment = this.getById(id);
        Optional<ApplicationDeploymentDetailView> detailOptional = Optional.ofNullable(ConverterUtil.convert(applicationDeployment, new ApplicationDeploymentDetailView()));
        if (detailOptional.isPresent()) {
            ApplicationDeploymentDetailView detail = detailOptional.get();
            Optional<List<BizApplicationDeploymentNetwork>> systemApplicationDeploymentNetworks = Optional.ofNullable(bizApplicationDeploymentNetworkService.list(
                    Wrappers.<BizApplicationDeploymentNetwork>lambdaQuery().eq(BizApplicationDeploymentNetwork::getDeploymentId, id).eq(BizApplicationDeploymentNetwork::getDelFlag, 0)));
            List<ApplicationDeploymentNetworkView> networks = ConverterUtil.convertList(BizApplicationDeploymentNetwork.class, ApplicationDeploymentNetworkView.class, systemApplicationDeploymentNetworks.orElseGet(ArrayList::new));
            Optional<List<BizApplicationDeploymentMount>> systemApplicationDeploymentMounts = Optional.ofNullable(bizApplicationDeploymentMountService.list(
                    Wrappers.<BizApplicationDeploymentMount>lambdaQuery().eq(BizApplicationDeploymentMount::getDeploymentId, id).eq(BizApplicationDeploymentMount::getDelFlag, 0)));
            List<ApplicationDeploymentMountView> mounts = ConverterUtil.convertList(BizApplicationDeploymentMount.class, ApplicationDeploymentMountView.class, systemApplicationDeploymentMounts.orElseGet(ArrayList::new));
            detail.setNetworks(networks);
            detail.setMounts(mounts);
            return detail;
        } else {
            return new ApplicationDeploymentDetailView();
        }
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(String namespaceId, BizApplicationDeployment bizApplicationDeployment) {
        bizApplicationDeployment.setNamespaceId(namespaceId);
        bizApplicationDeployment.setCreator(SecurityUtil.getUser().getId());
        bizApplicationDeployment.setCreateTime(LocalDateTime.now());
        if (this.save(bizApplicationDeployment)) {
            return bizApplicationDeployment.getId();
        } else {
            throw new RuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(String namespaceId, BizApplicationDeployment bizApplicationDeployment) {
        bizApplicationDeployment.setNamespaceId(namespaceId);
        bizApplicationDeployment.setModifier(SecurityUtil.getUser().getId());
        bizApplicationDeployment.setModifyTime(LocalDateTime.now());
        return this.updateById(bizApplicationDeployment);
    }

    @Override
    public Boolean update(BizApplicationDeployment bizApplicationDeployment) {
        bizApplicationDeployment.setModifier(SecurityUtil.getUser().getId());
        bizApplicationDeployment.setModifyTime(LocalDateTime.now());
        return this.updateById(bizApplicationDeployment);
    }

    @Override
    public Boolean updateForRobot(BizApplicationDeployment bizApplicationDeployment) {
        bizApplicationDeployment.setModifier("robot");
        bizApplicationDeployment.setModifyTime(LocalDateTime.now());
        return this.updateById(bizApplicationDeployment);
    }

}

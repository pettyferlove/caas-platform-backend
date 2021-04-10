package com.github.pettyfer.caas.framework.biz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeploymentNetwork;
import com.github.pettyfer.caas.framework.biz.mapper.BizApplicationDeploymentNetworkMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizApplicationDeploymentNetworkService;
import com.github.pettyfer.caas.framework.core.model.ApplicationDeploymentNetworkView;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 应用网络设置 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2020-07-28
 */
@Service
public class BizApplicationDeploymentNetworkServiceImpl extends ServiceImpl<BizApplicationDeploymentNetworkMapper, BizApplicationDeploymentNetwork> implements IBizApplicationDeploymentNetworkService {

    @Override
    public IPage<BizApplicationDeploymentNetwork> page(BizApplicationDeploymentNetwork bizApplicationDeploymentNetwork, Page<BizApplicationDeploymentNetwork> page) {
        return this.page(page, Wrappers.lambdaQuery(bizApplicationDeploymentNetwork).orderByDesc(BizApplicationDeploymentNetwork::getCreateTime));
    }

    @Override
    public BizApplicationDeploymentNetwork get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizApplicationDeploymentNetwork bizApplicationDeploymentNetwork) {
        bizApplicationDeploymentNetwork.setCreator(SecurityUtil.getUser().getId());
        bizApplicationDeploymentNetwork.setCreateTime(LocalDateTime.now());
        if (this.save(bizApplicationDeploymentNetwork)) {
            return bizApplicationDeploymentNetwork.getId();
        } else {
            throw new RuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizApplicationDeploymentNetwork bizApplicationDeploymentNetwork) {
        bizApplicationDeploymentNetwork.setModifier(SecurityUtil.getUser().getId());
        bizApplicationDeploymentNetwork.setModifyTime(LocalDateTime.now());
        return this.updateById(bizApplicationDeploymentNetwork);
    }

    @Override
    public void batchInsert(String deploymentId, List<ApplicationDeploymentNetworkView> deploymentNetworks) {
        Optional<List<BizApplicationDeploymentNetwork>> networksOptional = Optional.ofNullable(ConverterUtil.convertList(ApplicationDeploymentNetworkView.class, BizApplicationDeploymentNetwork.class, deploymentNetworks));
        if(networksOptional.isPresent()){
            List<BizApplicationDeploymentNetwork> networks = networksOptional.get().stream().peek(i -> {
                i.setDeploymentId(deploymentId);
                i.setCreator(SecurityUtil.getUser().getId());
                i.setCreateTime(LocalDateTime.now());
            }).collect(Collectors.toList());
            this.saveBatch(networks);
        }
    }

}

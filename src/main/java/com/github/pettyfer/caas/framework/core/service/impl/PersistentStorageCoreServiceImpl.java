package com.github.pettyfer.caas.framework.core.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.biz.entity.BizNamespace;
import com.github.pettyfer.caas.framework.biz.entity.BizPersistentStorage;
import com.github.pettyfer.caas.framework.biz.service.IBizNamespaceService;
import com.github.pettyfer.caas.framework.biz.service.IBizPersistentStorageService;
import com.github.pettyfer.caas.framework.core.model.PersistentStorageListView;
import com.github.pettyfer.caas.framework.core.model.PersistentStorageSelectView;
import com.github.pettyfer.caas.framework.core.service.IPersistentStorageCoreService;
import com.github.pettyfer.caas.framework.engine.kubernetes.service.IPersistentVolumeClaimService;
import com.github.pettyfer.caas.global.constants.EnvConstant;
import com.github.pettyfer.caas.global.constants.KubernetesConstant;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimBuilder;
import io.fabric8.kubernetes.api.model.Quantity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pettyfer
 */
@Slf4j
@Service
public class PersistentStorageCoreServiceImpl implements IPersistentStorageCoreService {

    private final IBizNamespaceService bizNamespaceService;

    private final IBizPersistentStorageService bizPersistentStorageService;

    private final IPersistentVolumeClaimService persistentVolumeClaimService;

    public PersistentStorageCoreServiceImpl(IBizNamespaceService bizNamespaceService, IBizPersistentStorageService bizPersistentStorageService, IPersistentVolumeClaimService persistentVolumeClaimService) {
        this.bizNamespaceService = bizNamespaceService;
        this.bizPersistentStorageService = bizPersistentStorageService;
        this.persistentVolumeClaimService = persistentVolumeClaimService;
    }

    @Override
    public IPage<PersistentStorageListView> page(String namespaceId, BizPersistentStorage persistentStorage, Page<BizPersistentStorage> page) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (namespaceOptional.isPresent()) {
            IPage<PersistentStorageListView> result = new Page<>();
            IPage<BizPersistentStorage> queryPage = bizPersistentStorageService.page(namespaceId, persistentStorage, page);
            List<PersistentStorageListView> mapList = queryPage.getRecords().stream().map(i -> {
                PersistentStorageListView persistentStorageListView = new PersistentStorageListView();
                ConverterUtil.convert(i, persistentStorageListView);
                return persistentStorageListView;
            }).collect(Collectors.toList());
            result.setRecords(mapList);
            result.setCurrent(queryPage.getCurrent());
            result.setSize(queryPage.getSize());
            result.setTotal(queryPage.getTotal());
            return result;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    public BizPersistentStorage get(String id) {
        Optional<BizPersistentStorage> persistentStorageOptional = Optional.ofNullable(bizPersistentStorageService.get(id));
        if (persistentStorageOptional.isPresent()) {
            Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(persistentStorageOptional.get().getNamespaceId()));
            if(!namespaceOptional.isPresent()){
                throw new BaseRuntimeException("命名空间不存在");
            }
            return persistentStorageOptional.get();
        } else {
            throw new BaseRuntimeException("存储配置不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean update(BizPersistentStorage persistentStorage) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(persistentStorage.getNamespaceId()));
        if (namespaceOptional.isPresent()) {
            PersistentVolumeClaim claim = buildPersistentVolumeClaim(persistentStorage);
            persistentVolumeClaimService.update(namespaceOptional.get().getName(), persistentStorage.getName(), claim);
            bizPersistentStorageService.update(persistentStorage);
            return true;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String create(BizPersistentStorage persistentStorage) {
        Optional<BizNamespace> namespaceOptional = Optional.ofNullable(bizNamespaceService.get(persistentStorage.getNamespaceId()));
        if (namespaceOptional.isPresent()) {
            String id = IdWorker.getIdStr();
            persistentStorage.setId(id);
            PersistentVolumeClaim claim = buildPersistentVolumeClaim(persistentStorage);
            persistentVolumeClaimService.create(namespaceOptional.get().getName(), claim);
            bizPersistentStorageService.create(persistentStorage);
            return id;
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    public List<PersistentStorageSelectView> select(String namespaceId, Integer envType) {
        Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(namespaceId));
        if (bizNamespaceOptional.isPresent()) {
            LambdaQueryWrapper<BizPersistentStorage> queryWrapper = Wrappers.<BizPersistentStorage>lambdaQuery();
            queryWrapper.eq(BizPersistentStorage::getDelFlag, 0);
            queryWrapper.eq(ObjectUtil.isNotNull(envType),BizPersistentStorage::getEnvType, envType);
            List<BizPersistentStorage> list = bizPersistentStorageService.list(queryWrapper);
            return Optional.ofNullable(ConverterUtil.convertList(BizPersistentStorage.class, PersistentStorageSelectView.class, list)).orElseGet(ArrayList::new);
        } else {
            throw new BaseRuntimeException("命名空间不存在");
        }
    }

    @Override
    public Boolean delete(String id) {
        Optional<BizPersistentStorage> persistentStorageOptional = Optional.ofNullable(bizPersistentStorageService.get(id));
        if (persistentStorageOptional.isPresent()) {
            Optional<BizNamespace> bizNamespaceOptional = Optional.ofNullable(bizNamespaceService.get(persistentStorageOptional.get().getNamespaceId()));
            if (bizNamespaceOptional.isPresent()) {
                persistentVolumeClaimService.delete(bizNamespaceOptional.get().getName(), persistentStorageOptional.get().getName());
            } else {
                throw new BaseRuntimeException("命名空间不存在");
            }
        } else {
            throw new BaseRuntimeException("存储配置不存在");
        }
        return bizPersistentStorageService.delete(id);
    }


    private PersistentVolumeClaim buildPersistentVolumeClaim(BizPersistentStorage persistentStorage) {
        Map<String, Quantity> request = new HashMap<>();
        Map<String, Quantity> limit = new HashMap<>();
        request.put("storage", new Quantity(persistentStorage.getInitSize(), persistentStorage.getUnit()));
        if (StrUtil.isNotEmpty(persistentStorage.getLimitSize())) {
            limit.put("storage", new Quantity(persistentStorage.getLimitSize(), persistentStorage.getUnit()));
        }
        return new PersistentVolumeClaimBuilder()
                .withNewMetadata()
                .withName(persistentStorage.getName())
                .withLabels(fetchLabel(persistentStorage.getName(), EnvConstant.transform(persistentStorage.getEnvType())))
                .endMetadata()
                .withNewSpec()
                .withStorageClassName(persistentStorage.getStorageClassName())
                .withNewResources()
                .withRequests(request)
                .withLimits(limit)
                .endResources()
                .withAccessModes(persistentStorage.getAccessMode().split(","))
                .endSpec()
                .build();
    }

    private Map<String, String> fetchLabel(String name, String envType) {
        Map<String, String> label = new HashMap<>();
        label.put(KubernetesConstant.K8S_LABEL, name);
        label.put(KubernetesConstant.GLOBAL_LABEL, name);
        label.put(KubernetesConstant.ENVIRONMENT_LABEL, envType);
        return label;
    }

}

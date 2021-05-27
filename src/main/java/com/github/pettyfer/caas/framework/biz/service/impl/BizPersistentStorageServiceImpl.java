package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizPersistentStorage;
import com.github.pettyfer.caas.framework.biz.mapper.BizPersistentStorageMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizPersistentStorageService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 持久化储存 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-21
 */
@Service
public class BizPersistentStorageServiceImpl extends ServiceImpl<BizPersistentStorageMapper, BizPersistentStorage> implements IBizPersistentStorageService {

    @Override
    public IPage<BizPersistentStorage> page(String namespaceId, BizPersistentStorage bizPersistentStorage, Page<BizPersistentStorage> page) {
        LambdaQueryWrapper<BizPersistentStorage> queryWrapper = Wrappers.<BizPersistentStorage>lambdaQuery()
                .eq(BizPersistentStorage::getNamespaceId, namespaceId)
                .likeRight(StrUtil.isNotEmpty(bizPersistentStorage.getName()), BizPersistentStorage::getName, bizPersistentStorage.getName())
                .eq(ObjectUtil.isNotNull(bizPersistentStorage.getEnvType()), BizPersistentStorage::getEnvType, bizPersistentStorage.getEnvType());
        return this.page(page, queryWrapper);
    }

    @Override
    public BizPersistentStorage get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizPersistentStorage bizPersistentStorage) {
        bizPersistentStorage.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizPersistentStorage.setCreateTime(LocalDateTime.now());
        if (this.save(bizPersistentStorage)) {
            return bizPersistentStorage.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizPersistentStorage bizPersistentStorage) {
        bizPersistentStorage.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizPersistentStorage.setModifyTime(LocalDateTime.now());
        return this.updateById(bizPersistentStorage);
    }

}

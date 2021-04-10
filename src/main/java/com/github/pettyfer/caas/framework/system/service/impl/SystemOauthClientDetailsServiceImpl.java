package com.github.pettyfer.caas.framework.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pettyfer.caas.framework.system.entity.SystemOauthClientDetails;
import com.github.pettyfer.caas.framework.system.mapper.SystemOauthClientDetailsMapper;
import com.github.pettyfer.caas.framework.system.service.ISystemOauthClientDetailsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 终端信息表 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-03-26
 */
@Service
public class SystemOauthClientDetailsServiceImpl extends ServiceImpl<SystemOauthClientDetailsMapper, SystemOauthClientDetails> implements ISystemOauthClientDetailsService {

    @Override
    public IPage<SystemOauthClientDetails> page(SystemOauthClientDetails systemOauthClientDetails, Page<SystemOauthClientDetails> page) {
        return this.page(page, Wrappers.lambdaQuery(systemOauthClientDetails).orderByDesc(SystemOauthClientDetails::getCreateTime));
    }

    @Override
    public SystemOauthClientDetails get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(SystemOauthClientDetails systemOauthClientDetails) {
        systemOauthClientDetails.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemOauthClientDetails.setCreateTime(LocalDateTime.now());
        if (this.save(systemOauthClientDetails)) {
            return systemOauthClientDetails.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(SystemOauthClientDetails systemOauthClientDetails) {
        systemOauthClientDetails.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemOauthClientDetails.setModifyTime(LocalDateTime.now());
        return this.updateById(systemOauthClientDetails);
    }

}

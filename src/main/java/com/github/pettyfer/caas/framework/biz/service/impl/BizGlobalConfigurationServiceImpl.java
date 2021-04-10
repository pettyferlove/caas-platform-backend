package com.github.pettyfer.caas.framework.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizGlobalConfiguration;
import com.github.pettyfer.caas.framework.biz.mapper.BizGlobalConfigurationMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizGlobalConfigurationService;
import com.github.pettyfer.caas.framework.core.model.GlobalConfiguration;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2020-06-29
 */
@Slf4j
@Service
public class BizGlobalConfigurationServiceImpl extends ServiceImpl<BizGlobalConfigurationMapper, BizGlobalConfiguration> implements IBizGlobalConfigurationService {


    @Override
    public BizGlobalConfiguration get() {
        LambdaQueryWrapper<BizGlobalConfiguration> wrapper = Wrappers.<BizGlobalConfiguration>lambdaQuery();
        return this.getOne(wrapper);
    }

    @Override
    public String create(BizGlobalConfiguration bizGlobalConfiguration) {
        try {
            bizGlobalConfiguration.setCreator(SecurityUtil.getUser().getId());
            bizGlobalConfiguration.setCreateTime(LocalDateTime.now());
            if (this.save(bizGlobalConfiguration)) {
                return bizGlobalConfiguration.getId();
            } else {
                throw new RuntimeException("新增失败");
            }
        } catch (Exception e) {
            throw new RuntimeException("服务异常");
        }
    }

    @Override
    public Boolean update(BizGlobalConfiguration bizGlobalConfiguration) {
        try {
            bizGlobalConfiguration.setModifier(SecurityUtil.getUser().getId());
            bizGlobalConfiguration.setModifyTime(LocalDateTime.now());
            return this.updateById(bizGlobalConfiguration);
        } catch (Exception e) {
            throw new RuntimeException("服务异常");
        }
    }

    @Override
    public GlobalConfiguration loadConfig() {
        LambdaQueryWrapper<BizGlobalConfiguration> wrapper = Wrappers.<BizGlobalConfiguration>lambdaQuery();
        BizGlobalConfiguration bizGlobalConfiguration = this.getOne(wrapper);
        Optional<GlobalConfiguration> convert = Optional.ofNullable(ConverterUtil.convert(bizGlobalConfiguration, new GlobalConfiguration()));
        return convert.orElseGet(GlobalConfiguration::new);
    }

}

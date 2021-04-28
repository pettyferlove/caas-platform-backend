package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizUserConfiguration;
import com.github.pettyfer.caas.framework.biz.mapper.BizUserConfigurationMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizUserConfigurationService;
import com.github.pettyfer.caas.framework.core.model.UserConfiguration;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.SecurityUtil;
import com.google.common.base.Preconditions;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import lombok.extern.slf4j.Slf4j;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabSSHKey;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-03-29
 */
@Slf4j
@Service
public class BizUserConfigurationServiceImpl extends ServiceImpl<BizUserConfigurationMapper, BizUserConfiguration> implements IBizUserConfigurationService {

    @Override
    public BizUserConfiguration get() {
        LambdaQueryWrapper<BizUserConfiguration> wrapper = Wrappers.<BizUserConfiguration>lambdaQuery().eq(BizUserConfiguration::getCreator, SecurityUtil.getUser().getId());
        return this.getOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String create(BizUserConfiguration bizUserConfiguration) {
        int type = KeyPair.RSA;
        JSch jsch = new JSch();
        String keyTitle = SecurityUtil.getUser().getUsername() + "_public_key_" + RandomUtil.randomString(5);
        GitlabAPI gitlabAPI = GitlabAPI.connect(bizUserConfiguration.getGitlabHomePath(), bizUserConfiguration.getGitlabApiToken());
        try {
            KeyPair keyPair = KeyPair.genKeyPair(jsch, type);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            keyPair.writePrivateKey(baos);
            String privateKeyString = baos.toString();
            baos = new ByteArrayOutputStream();
            keyPair.writePublicKey(baos, keyTitle);
            String publicKeyString = baos.toString();
            keyPair.dispose();
            GitlabSSHKey sshKey = gitlabAPI.createSSHKey(keyTitle, publicKeyString);
            bizUserConfiguration.setPrivateKey(privateKeyString);
            bizUserConfiguration.setUserKeyId(sshKey.getId());
        } catch (Exception e) {
            throw new BaseRuntimeException("密钥生成失败");
        }

        bizUserConfiguration.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizUserConfiguration.setCreateTime(LocalDateTime.now());
        if (this.save(bizUserConfiguration)) {
            return bizUserConfiguration.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizUserConfiguration bizUserConfiguration) {
        bizUserConfiguration.setPrivateKey(null);
        bizUserConfiguration.setUserKeyId(null);
        bizUserConfiguration.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizUserConfiguration.setModifyTime(LocalDateTime.now());
        return this.updateById(bizUserConfiguration);
    }

    @Override
    public UserConfiguration loadConfig() {
        LambdaQueryWrapper<BizUserConfiguration> wrapper = Wrappers.<BizUserConfiguration>lambdaQuery().eq(BizUserConfiguration::getCreator, SecurityUtil.getUser().getId());
        BizUserConfiguration bizUserConfiguration = this.getOne(wrapper);
        Optional<UserConfiguration> convert = Optional.ofNullable(ConverterUtil.convert(bizUserConfiguration, new UserConfiguration()));
        return convert.orElseGet(UserConfiguration::new);
    }

    @Override
    public UserConfiguration loadConfig(String userId) {
        LambdaQueryWrapper<BizUserConfiguration> wrapper = Wrappers.<BizUserConfiguration>lambdaQuery().eq(BizUserConfiguration::getCreator, userId);
        BizUserConfiguration bizUserConfiguration = this.getOne(wrapper);
        Optional<UserConfiguration> convert = Optional.ofNullable(ConverterUtil.convert(bizUserConfiguration, new UserConfiguration()));
        return convert.orElseGet(UserConfiguration::new);
    }

    @Override
    public Boolean checkConfiguration() {
        LambdaQueryWrapper<BizUserConfiguration> wrapper = Wrappers.<BizUserConfiguration>lambdaQuery().eq(BizUserConfiguration::getCreator, SecurityUtil.getUser().getId());
        BizUserConfiguration bizUserConfiguration = this.getOne(wrapper);
        if (ObjectUtil.isNull(bizUserConfiguration)) {
            throw new BaseRuntimeException("尚未填写个人设置，请先完善系统设置");
        }
        Preconditions.checkNotNull(bizUserConfiguration.getGitlabHomePath(), "未配置Gitlab地址");
        Preconditions.checkNotNull(bizUserConfiguration.getGitlabApiToken(), "未配置Gitlab Api Token");
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean refresh() {
        LambdaQueryWrapper<BizUserConfiguration> queryWrapper = Wrappers.<BizUserConfiguration>lambdaQuery();
        queryWrapper.eq(BizUserConfiguration::getCreator, SecurityUtil.getUser().getId());
        queryWrapper.eq(BizUserConfiguration::getDelFlag, false);
        BizUserConfiguration bizUserConfiguration = this.getOne(queryWrapper);
        if (ObjectUtil.isNull(bizUserConfiguration)) {
            throw new BaseRuntimeException("尚未填写个人设置，请先完善系统设置");
        }
        String keyTitle = SecurityUtil.getUser().getUsername() + "_public_key_" + RandomUtil.randomString(5);
        GitlabAPI gitlabAPI = GitlabAPI.connect(bizUserConfiguration.getGitlabHomePath(), bizUserConfiguration.getGitlabApiToken());
        try {
            GitlabSSHKey sshKey = gitlabAPI.getSSHKey(bizUserConfiguration.getUserKeyId());
            gitlabAPI.deleteSSHKey(sshKey.getUser().getId(), sshKey.getId());
        } catch (Exception e) {
            log.warn("SSHKey delete fails the exception will be ignored");
        }
        int type = KeyPair.RSA;
        JSch jsch = new JSch();
        try {
            KeyPair keyPair = KeyPair.genKeyPair(jsch, type);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            keyPair.writePrivateKey(baos);
            String privateKeyString = baos.toString();
            baos = new ByteArrayOutputStream();
            keyPair.writePublicKey(baos, keyTitle);
            String publicKeyString = baos.toString();
            keyPair.dispose();
            GitlabSSHKey sshKey = gitlabAPI.createSSHKey(keyTitle, publicKeyString);

            LambdaUpdateWrapper<BizUserConfiguration> updateWrapper = Wrappers.<BizUserConfiguration>lambdaUpdate();
            updateWrapper.set(BizUserConfiguration::getPrivateKey, privateKeyString);
            updateWrapper.set(BizUserConfiguration::getUserKeyId, sshKey.getId());
            updateWrapper.eq(BizUserConfiguration::getId, bizUserConfiguration.getId());
            this.update(updateWrapper);
        } catch (Exception e) {
            throw new BaseRuntimeException("密钥生成失败，请检查GitLab设置");
        }

        return true;
    }

}

package com.github.pettyfer.caas.framework.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.system.entity.SystemAttachmentInfo;
import com.github.pettyfer.caas.framework.system.mapper.SystemAttachmentInfoMapper;
import com.github.pettyfer.caas.framework.system.model.Upload;
import com.github.pettyfer.caas.framework.system.service.ISystemAttachmentInfoService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-02
 */
@Service
public class SystemAttachmentInfoServiceImpl extends ServiceImpl<SystemAttachmentInfoMapper, SystemAttachmentInfo> implements ISystemAttachmentInfoService {

    @Override
    public IPage<SystemAttachmentInfo> page(SystemAttachmentInfo systemAttachmentInfo, Page<SystemAttachmentInfo> page) {
        return this.page(page, Wrappers.lambdaQuery(systemAttachmentInfo).orderByDesc(SystemAttachmentInfo::getCreateTime));
    }

    @Override
    public SystemAttachmentInfo get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(SystemAttachmentInfo systemAttachmentInfo) {
        systemAttachmentInfo.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemAttachmentInfo.setCreateTime(LocalDateTime.now());
        if (this.save(systemAttachmentInfo)) {
            return systemAttachmentInfo.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(SystemAttachmentInfo systemAttachmentInfo) {
        systemAttachmentInfo.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        systemAttachmentInfo.setModifyTime(LocalDateTime.now());
        return this.updateById(systemAttachmentInfo);
    }

    @Override
    public Boolean save(String userId, String fileId, String fileName, String md5, String filePath, Upload upload, String contentType, Long fileSize) {
        SystemAttachmentInfo attachmentInfo = new SystemAttachmentInfo();
        attachmentInfo.setId(fileId);
        attachmentInfo.setMd5(md5);
        attachmentInfo.setCreator(userId);
        attachmentInfo.setCreateTime(LocalDateTime.now());
        attachmentInfo.setFileName(fileName);
        attachmentInfo.setFileType(contentType);
        attachmentInfo.setSize(fileSize);
        attachmentInfo.setStorageType(upload.getStorage().getValue());
        attachmentInfo.setPath(filePath);
        return this.save(attachmentInfo);
    }

}

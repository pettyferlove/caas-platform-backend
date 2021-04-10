package com.github.pettyfer.caas.framework.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.system.entity.SystemAttachmentInfo;
import com.github.pettyfer.caas.framework.system.model.Upload;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-04-02
 */
public interface ISystemAttachmentInfoService extends IService<SystemAttachmentInfo> {

    /**
     * List查找
     *
     * @param systemAttachmentInfo 查询参数对象
     * @param page                 Page分页对象
     * @return IPage 返回结果
     */
    IPage<SystemAttachmentInfo> page(SystemAttachmentInfo systemAttachmentInfo, Page<SystemAttachmentInfo> page);

    /**
     * 通过Id查询SystemAttachmentInfo信息
     *
     * @param id 业务主键
     * @return 对象
     */
    SystemAttachmentInfo get(String id);

    /**
     * 通过Id删除信息
     *
     * @param id 业务主键
     * @return Boolean
     */
    Boolean delete(String id);

    /**
     * 创建数据
     *
     * @param systemAttachmentInfo 要创建的对象
     * @return Boolean
     */
    String create(SystemAttachmentInfo systemAttachmentInfo);

    /**
     * 更新数据（必须带Id）
     *
     * @param systemAttachmentInfo 对象
     * @return Boolean
     */
    Boolean update(SystemAttachmentInfo systemAttachmentInfo);

    Boolean save(String userId, String fileId, String fileName, String md5, String filePath, Upload upload, String contentType, Long fileSize);

}

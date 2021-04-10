package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizImagesDepository;
import com.github.pettyfer.caas.framework.core.model.ImagesDepositorySelect;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Petty
 * @since 2020-07-13
 */
public interface IBizImagesDepositoryService extends IService<BizImagesDepository> {

    /**
     * List查找
     *
     * @param bizImagesDepository 查询参数对象
     * @param page                   Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizImagesDepository> page(BizImagesDepository bizImagesDepository, Page<BizImagesDepository> page);

    /**
     * 通过Id查询SystemImagesDepository信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizImagesDepository get(String id);

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
     * @param bizImagesDepository 要创建的对象
     * @return Boolean
     */
    String create(BizImagesDepository bizImagesDepository);

    /**
     * 创建镜像仓库与当前用户的映射
     * @param projectId projectId
     * @param projectName projectName
     */
    void create(String projectId, String projectName);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizImagesDepository 对象
     * @return Boolean
     */
    Boolean update(BizImagesDepository bizImagesDepository);

    /**
     * 获取镜像仓库列表
     * @return 集合
     */
    List<ImagesDepositorySelect> select();
}

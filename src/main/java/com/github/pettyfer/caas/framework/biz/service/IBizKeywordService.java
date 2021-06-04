package com.github.pettyfer.caas.framework.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pettyfer.caas.framework.biz.entity.BizKeyword;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Petty
 * @since 2021-06-02
 */
public interface IBizKeywordService extends IService<BizKeyword> {

    /**
     * List查找
     *
     * @param bizKeyword 查询参数对象
     * @param page       Page分页对象
     * @return IPage 返回结果
     */
    IPage<BizKeyword> page(BizKeyword bizKeyword, Page<BizKeyword> page);

    /**
     * 通过Id查询BizKeyword信息
     *
     * @param id 业务主键
     * @return 对象
     */
    BizKeyword get(String id);

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
     * @param bizKeyword 要创建的对象
     * @return Boolean
     */
    String create(BizKeyword bizKeyword);

    /**
     * 更新数据（必须带Id）
     *
     * @param bizKeyword 对象
     * @return Boolean
     */
    Boolean update(BizKeyword bizKeyword);

    List<BizKeyword> selected(String bizId, String bizType);

    List<BizKeyword> allSelected();
}

package com.github.pettyfer.caas.framework.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuild;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildListView;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 自动构建 Mapper 接口
 * </p>
 *
 * @author Petty
 * @since 2020-07-03
 */
public interface BizProjectBuildMapper extends BaseMapper<BizProjectBuild> {

    /**
     * 获取自动构建列表
     * @param page 分页查询参数
     * @param queryWrapper 查询条件包装器
     * @return 分页查询结果
     */
    IPage<ProjectBuildListView> queryProjectBuildList(IPage<BizProjectBuild> page, @Param("ew") Wrapper<BizProjectBuild> queryWrapper);
}

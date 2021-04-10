package com.github.pettyfer.caas.framework.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuild;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pettyfer.caas.framework.core.model.SqlBuildListView;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * SQL构建 Mapper 接口
 * </p>
 *
 * @author Petty
 * @since 2021-04-01
 */
public interface BizSqlBuildMapper extends BaseMapper<BizSqlBuild> {

    IPage<SqlBuildListView> page(IPage<BizSqlBuild> page, @Param("ew") Wrapper<BizSqlBuild> params);

}

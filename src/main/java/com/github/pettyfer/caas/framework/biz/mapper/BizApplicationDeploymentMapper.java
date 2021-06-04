package com.github.pettyfer.caas.framework.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pettyfer.caas.framework.biz.entity.BizApplicationDeployment;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 应用部署 Mapper 接口
 * </p>
 *
 * @author Petty
 * @since 2021-04-20
 */
public interface BizApplicationDeploymentMapper extends BaseMapper<BizApplicationDeployment> {

    IPage<BizApplicationDeployment> page(IPage<BizApplicationDeployment> page, @Param("ew") Wrapper<BizApplicationDeployment> queryWrapper);

}

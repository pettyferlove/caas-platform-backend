package com.github.pettyfer.caas.framework.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pettyfer.caas.framework.biz.entity.BizConfig;
import com.github.pettyfer.caas.framework.core.model.ConfigListView;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 配置文件管理 Mapper 接口
 * </p>
 *
 * @author Petty
 * @since 2021-04-09
 */
public interface BizConfigMapper extends BaseMapper<BizConfig> {

    IPage<ConfigListView> page(IPage<BizConfig> page, @Param("ew") Wrapper<BizConfig> params);

}

package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizSqlBuildHistory;
import com.github.pettyfer.caas.framework.biz.mapper.BizSqlBuildHistoryMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizSqlBuildHistoryService;
import com.github.pettyfer.caas.framework.core.model.SqlBuildHistorySelectView;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * SQL构建历史记录 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-01
 */
@Service
public class BizSqlBuildHistoryServiceImpl extends ServiceImpl<BizSqlBuildHistoryMapper, BizSqlBuildHistory> implements IBizSqlBuildHistoryService {

    @Override
    public IPage<BizSqlBuildHistory> page(BizSqlBuildHistory bizSqlBuildHistory, Page<BizSqlBuildHistory> page) {
        LambdaQueryWrapper<BizSqlBuildHistory> queryWrapper = Wrappers.<BizSqlBuildHistory>lambdaQuery().orderByDesc(BizSqlBuildHistory::getCreateTime);
        queryWrapper.eq(StrUtil.isNotEmpty(bizSqlBuildHistory.getBuildId()), BizSqlBuildHistory::getBuildId, bizSqlBuildHistory.getBuildId());
        return this.page(page, queryWrapper);
    }

    @Override
    public BizSqlBuildHistory get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizSqlBuildHistory bizSqlBuildHistory) {
        bizSqlBuildHistory.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizSqlBuildHistory.setCreateTime(LocalDateTime.now());
        if (this.save(bizSqlBuildHistory)) {
            return bizSqlBuildHistory.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizSqlBuildHistory bizSqlBuildHistory) {
        bizSqlBuildHistory.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizSqlBuildHistory.setModifyTime(LocalDateTime.now());
        return this.updateById(bizSqlBuildHistory);
    }

    @Override
    public List<SqlBuildHistorySelectView> historySelect(String id) {
        return baseMapper.historySelect(id);
    }

}

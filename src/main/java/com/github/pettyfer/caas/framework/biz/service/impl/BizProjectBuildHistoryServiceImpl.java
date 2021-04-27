package com.github.pettyfer.caas.framework.biz.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizProjectBuildHistory;
import com.github.pettyfer.caas.framework.biz.mapper.BizProjectBuildHistoryMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizProjectBuildHistoryService;
import com.github.pettyfer.caas.framework.core.model.ProjectBuildHistorySelectView;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 项目构建历史记录 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-04-08
 */
@Service
public class BizProjectBuildHistoryServiceImpl extends ServiceImpl<BizProjectBuildHistoryMapper, BizProjectBuildHistory> implements IBizProjectBuildHistoryService {

    @Override
    public IPage<BizProjectBuildHistory> page(BizProjectBuildHistory bizProjectBuildHistory, Page<BizProjectBuildHistory> page) {
        return this.page(page, Wrappers.lambdaQuery(bizProjectBuildHistory).orderByDesc(BizProjectBuildHistory::getCreateTime));
    }

    @Override
    public BizProjectBuildHistory get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizProjectBuildHistory bizProjectBuildHistory) {
        String userId = "robot";
        if (ObjectUtil.isNotNull(SecurityUtil.getUser())) {
            userId = SecurityUtil.getUser().getId();
        }
        bizProjectBuildHistory.setCreator(userId);
        bizProjectBuildHistory.setCreateTime(LocalDateTime.now());
        if (this.save(bizProjectBuildHistory)) {
            return bizProjectBuildHistory.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizProjectBuildHistory bizProjectBuildHistory) {
        bizProjectBuildHistory.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizProjectBuildHistory.setModifyTime(LocalDateTime.now());
        return this.updateById(bizProjectBuildHistory);
    }

    @Override
    public List<ProjectBuildHistorySelectView> historySelect(String id) {
        return baseMapper.historySelect(id);
    }

    @Override
    public BizProjectBuildHistory queryLastBuild(String id) {
        return baseMapper.queryLastBuild(id);
    }

}

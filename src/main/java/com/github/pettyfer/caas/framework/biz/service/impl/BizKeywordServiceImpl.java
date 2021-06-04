package com.github.pettyfer.caas.framework.biz.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizKeyword;
import com.github.pettyfer.caas.framework.biz.mapper.BizKeywordMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizKeywordService;
import com.github.pettyfer.caas.global.exception.BaseRuntimeException;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2021-06-02
 */
@Service
public class BizKeywordServiceImpl extends ServiceImpl<BizKeywordMapper, BizKeyword> implements IBizKeywordService {

    @Override
    public IPage<BizKeyword> page(BizKeyword bizKeyword, Page<BizKeyword> page) {
        return this.page(page, Wrappers.lambdaQuery(bizKeyword).orderByDesc(BizKeyword::getCreateTime));
    }

    @Override
    public BizKeyword get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizKeyword bizKeyword) {
        bizKeyword.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizKeyword.setCreateTime(LocalDateTime.now());
        if (this.save(bizKeyword)) {
            return bizKeyword.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(BizKeyword bizKeyword) {
        bizKeyword.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        bizKeyword.setModifyTime(LocalDateTime.now());
        return this.updateById(bizKeyword);
    }

    @Override
    public List<BizKeyword> selected(String bizId, String bizType) {
        return baseMapper.selected(bizId, bizType);
    }

    @Override
    public List<BizKeyword> allSelected() {
        return baseMapper.allSelected();
    }

}

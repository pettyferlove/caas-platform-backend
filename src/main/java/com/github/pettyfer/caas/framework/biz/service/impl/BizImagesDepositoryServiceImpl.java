package com.github.pettyfer.caas.framework.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pettyfer.caas.framework.biz.entity.BizImagesDepository;
import com.github.pettyfer.caas.framework.biz.mapper.BizImagesDepositoryMapper;
import com.github.pettyfer.caas.framework.biz.service.IBizImagesDepositoryService;
import com.github.pettyfer.caas.framework.core.model.ImagesDepositorySelect;
import com.github.pettyfer.caas.utils.ConverterUtil;
import com.github.pettyfer.caas.utils.SecurityUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Petty
 * @since 2020-07-13
 */
@Service
public class BizImagesDepositoryServiceImpl extends ServiceImpl<BizImagesDepositoryMapper, BizImagesDepository> implements IBizImagesDepositoryService {

    @Override
    public IPage<BizImagesDepository> page(BizImagesDepository bizImagesDepository, Page<BizImagesDepository> page) {
        return this.page(page, Wrappers.lambdaQuery(bizImagesDepository).orderByDesc(BizImagesDepository::getCreateTime));
    }

    @Override
    public BizImagesDepository get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(BizImagesDepository bizImagesDepository) {
        bizImagesDepository.setCreator(SecurityUtil.getUser().getId());
        bizImagesDepository.setCreateTime(LocalDateTime.now());
        if (this.save(bizImagesDepository)) {
            return bizImagesDepository.getId();
        } else {
            throw new RuntimeException("新增失败");
        }
    }

    @Override
    public void create(String projectId, String projectName) {
        Optional<BizImagesDepository> systemImagesDepositoryOptional = Optional.ofNullable(this.getOne(Wrappers.<BizImagesDepository>lambdaQuery().eq(BizImagesDepository::getProjectId, projectId)));
        if(!systemImagesDepositoryOptional.isPresent()){
            BizImagesDepository create = new BizImagesDepository();
            create.setProjectId(projectId);
            create.setProjectName(projectName);
            this.create(create);
        }
    }

    @Override
    public Boolean update(BizImagesDepository bizImagesDepository) {
        bizImagesDepository.setModifier(SecurityUtil.getUser().getId());
        bizImagesDepository.setModifyTime(LocalDateTime.now());
        return this.updateById(bizImagesDepository);
    }

    @Override
    public List<ImagesDepositorySelect> select() {
        LambdaQueryWrapper<BizImagesDepository> queryWrapper = Wrappers.<BizImagesDepository>lambdaQuery()
                .orderByDesc(BizImagesDepository::getCreateTime)
                .eq(BizImagesDepository::getDelFlag, 0)
                .eq(BizImagesDepository::getCreator, SecurityUtil.getUser().getId());
        List<BizImagesDepository> list = this.list(queryWrapper);
        Optional<List<ImagesDepositorySelect>> selects = Optional.ofNullable(ConverterUtil.convertList(BizImagesDepository.class, ImagesDepositorySelect.class, list));
        return selects.orElseGet(ArrayList::new);
    }

}

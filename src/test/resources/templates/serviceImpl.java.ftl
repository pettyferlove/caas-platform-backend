package ${package.ServiceImpl};

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import BaseRuntimeException;
import SecurityUtil;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Override
    public IPage<${entity}> page(${entity} ${entity?uncap_first}, Page<${entity}> page) {
        return this.page(page, Wrappers.lambdaQuery(${entity?uncap_first}).orderByDesc(${entity}::getCreateTime));
    }

    @Override
    public ${entity} get(String id) {
        return this.getById(id);
    }

    @Override
    public Boolean delete(String id) {
        return this.removeById(id);
    }

    @Override
    public String create(${entity} ${entity?uncap_first}) {
        ${entity?uncap_first}.setCreator(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        ${entity?uncap_first}.setCreateTime(LocalDateTime.now());
        if (this.save(${entity?uncap_first})) {
            return ${entity?uncap_first}.getId();
        } else {
            throw new BaseRuntimeException("新增失败");
        }
    }

    @Override
    public Boolean update(${entity} ${entity?uncap_first}) {
        ${entity?uncap_first}.setModifier(Objects.requireNonNull(SecurityUtil.getUser()).getId());
        ${entity?uncap_first}.setModifyTime(LocalDateTime.now());
        return this.updateById(${entity?uncap_first});
    }

}
</#if>

package ${package.Controller};


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import R;
import org.springframework.web.bind.annotation.*;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import io.swagger.annotations.*;

/**
 * <p>
 * ${table.comment!} 接口控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Api(value = "${table.comment}", tags = {"${table.comment}接口"})
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>
    private final ${table.serviceName} ${entity?uncap_first}Service;

    public ${table.controllerName}(${table.serviceName} ${entity?uncap_first}Service) {
        this.${entity?uncap_first}Service = ${entity?uncap_first}Service;
    }

    @GetMapping("page")
    public R<IPage> page(${entity} ${entity?uncap_first}, Page<${entity}> page) {
        return new R<>(${entity?uncap_first}Service.page(${entity?uncap_first}, page));
    }


    @GetMapping("/{id}")
    public R<${entity}> get(@PathVariable String id) {
        return new R<>(${entity?uncap_first}Service.get(id));
    }

    public R<String> create(${entity} ${entity?uncap_first}) {
        return new R<>(${entity?uncap_first}Service.create(${entity?uncap_first}));
    }

    @PutMapping
    public R<Boolean> update(${entity} ${entity?uncap_first}) {
        return new R<>(${entity?uncap_first}Service.update(${entity?uncap_first}));
    }

    @DeleteMapping("/{id}")
    public R<Boolean> delete(@PathVariable String id) {
        return new R<>(${entity?uncap_first}Service.delete(id));
    }
}
</#if>

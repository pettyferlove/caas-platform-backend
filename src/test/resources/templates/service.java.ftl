package ${package.Service};

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package.Entity}.${entity};
import ${superServiceClassPackage};

/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {

    /**
     * List查找
     *
     * @param ${entity?uncap_first} 查询参数对象
     * @param page     Page分页对象
     * @return IPage 返回结果
     */
    IPage<${entity}> page(${entity} ${entity?uncap_first}, Page<${entity}> page);

    /**
     * 通过Id查询${entity?cap_first}信息
     *
     * @param id 业务主键
     * @return 对象
     */
    ${entity} get(String id);

     /**
      * 通过Id删除信息
      *
      * @param id 业务主键
      * @return Boolean
      */
     Boolean delete(String id);

     /**
      * 创建数据
      *
      * @param ${entity?uncap_first} 要创建的对象
      * @return Boolean
      */
     String create(${entity} ${entity?uncap_first});

     /**
      * 更新数据（必须带Id）
      *
      * @param ${entity?uncap_first} 对象
      * @return Boolean
      */
     Boolean update(${entity} ${entity?uncap_first});

}
</#if>

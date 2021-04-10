package com.github.pettyfer.caas.framework.biz.entity;

import com.github.pettyfer.caas.global.entity.BaseEntity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <p>
 * 配置文件管理
 * </p>
 *
 * @author Petty
 * @since 2021-04-09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="BizConfig对象", description="配置文件管理")
public class BizConfig extends BaseEntity<BizConfig> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "命名空间ID")
    private String namespaceId;

    @ApiModelProperty(value = "配置名称")
    private String configName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "配置文件名称")
    private String fileName;

    @ApiModelProperty(value = "配置类型")
    private String configType;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "环境信息")
    private Integer envType;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

package com.github.pettyfer.caas.framework.biz.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pettyfer.caas.global.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 服务发现
 * </p>
 *
 * @author Petty
 * @since 2021-04-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "BizServiceDiscovery对象", description = "服务发现")
public class BizServiceDiscovery extends BaseEntity<BizServiceDiscovery> {

    private static final long serialVersionUID = 1L;

    private String deploymentId;

    private String namespaceId;

    @ApiModelProperty(value = "服务发现名称")
    private String name;

    @ApiModelProperty(value = "网络设置")
    private String network;

    @ApiModelProperty(value = "匹配标签")
    private String matchLabel;

    @ApiModelProperty(value = "网络类型")
    private String networkType;

    @ApiModelProperty(value = "外部访问IP，以英文,分隔")
    private String externalIp;

    @ApiModelProperty(value = "端口映射表")
    private String ports;

    @ApiModelProperty(value = "环境类型")
    private Integer envType;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

    public void setId(Object o) {
    }
}

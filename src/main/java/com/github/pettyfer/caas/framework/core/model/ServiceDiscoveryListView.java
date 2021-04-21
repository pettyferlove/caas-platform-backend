package com.github.pettyfer.caas.framework.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "服务治理Model", description = "")
public class ServiceDiscoveryListView {

    private String id;

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

    @ApiModelProperty(value = "内部端点")
    private String internalEndpoints;

    @ApiModelProperty(value = "外部端点")
    private String externalEndpoints;

    @ApiModelProperty(value = "集群IP")
    private String clusterIp;

    @ApiModelProperty(value = "环境类型")
    private Integer envType;

}

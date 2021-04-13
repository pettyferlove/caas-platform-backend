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
 * 应用网络设置
 * </p>
 *
 * @author Petty
 * @since 2021-04-12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="BizApplicationDeploymentNetwork对象", description="应用网络设置")
public class BizApplicationDeploymentNetwork extends BaseEntity<BizApplicationDeploymentNetwork> {

    private static final long serialVersionUID = 1L;

    private String deploymentId;

    @ApiModelProperty(value = "通讯协议")
    private String protocol;

    @ApiModelProperty(value = "端口号")
    private Integer port;

    @ApiModelProperty(value = "目标端口号")
    private Integer targetPort;

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

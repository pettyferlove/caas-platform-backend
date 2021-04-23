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
 *
 * </p>
 *
 * @author Petty
 * @since 2021-04-13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "BizApplicationDeploymentVolume对象", description = "")
public class BizApplicationDeploymentMount extends BaseEntity<BizApplicationDeploymentMount> {

    private static final long serialVersionUID = 1L;

    private String deploymentId;

    @ApiModelProperty(value = "挂载名称")
    private String mountName;

    @ApiModelProperty(value = "挂载地址")
    private String mountPath;

    @ApiModelProperty(value = "卷名")
    private String volumeName;

    @ApiModelProperty(value = "卷类型（配置文件、目录、空目录、主机目录）")
    private String volumeType;

    @ApiModelProperty(value = "卷地址（空目录和配置文件不需指定）")
    private String volumePath;

    @ApiModelProperty(value = "配置文件ID")
    private String configId;

    @ApiModelProperty(value = "持久化存储ID")
    private String persistentStorageId;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

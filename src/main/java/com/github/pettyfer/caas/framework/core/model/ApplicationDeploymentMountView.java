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
@ApiModel(value = "应用挂载", description = "")
public class ApplicationDeploymentMountView {

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

}

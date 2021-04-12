package com.github.pettyfer.caas.framework.core.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="全局配置Model", description="")
public class GlobalConfiguration implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Docker镜像私有仓库地址")
    private String dockerRegistryPath;

    @ApiModelProperty(value = "Docker镜像私有仓库用户名")
    private String dockerRegistryUsername;

    @ApiModelProperty(value = "Docker镜像私有仓库用户密码")
    private String dockerRegistryPassword;

    @ApiModelProperty(value = "CAAS集群地址;号分隔")
    private String clusterServer;

}

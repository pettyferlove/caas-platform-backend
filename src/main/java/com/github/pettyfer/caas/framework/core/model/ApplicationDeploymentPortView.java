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
@ApiModel(value = "应用部署端口映射", description = "")
public class ApplicationDeploymentPortView implements Serializable {
    private static final long serialVersionUID = 1355874754009669487L;

    @ApiModelProperty(value = "通讯协议")
    private String protocol;

    @ApiModelProperty(value = "端口号")
    private Integer port;

    @ApiModelProperty(value = "目标端口号")
    private Integer targetPort;

}

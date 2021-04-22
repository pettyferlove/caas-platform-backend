package com.github.pettyfer.caas.framework.core.model;

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
@ApiModel(value = "自动构建下拉框", description = "")
public class ProjectBuildSelect implements Serializable {

    private static final long serialVersionUID = 5065510649631702102L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "镜像仓库别名")
    private String imagesDepositoryAlias;

    private String imagesDepositoryId;

    @ApiModelProperty(value = "是否构建镜像")
    private Integer needBuildImage;

}

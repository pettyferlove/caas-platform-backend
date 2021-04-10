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
@ApiModel(value = "镜像仓库下拉框", description = "")
public class ImagesDepositorySelect implements Serializable {
    private static final long serialVersionUID = 7224072107722299910L;

    @ApiModelProperty(value = "镜像仓库ID")
    private String projectId;

    @ApiModelProperty(value = "镜像仓库名称")
    private String projectName;

}

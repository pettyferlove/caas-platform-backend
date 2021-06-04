package com.github.pettyfer.caas.framework.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "应用部署Model", description = "")
public class ApplicationDeploymentListView implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private String id;

    private String keywords;

    @ApiModelProperty(value = "镜像名称")
    private String imageName;

    @ApiModelProperty(value = "镜像标签")
    private String imageTag;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "副本数")
    private Integer replicas;

    @ApiModelProperty(value = "就绪副本数")
    private Integer readyReplicas;

    @ApiModelProperty(value = "实例数量")
    private Integer instancesNumber;

    @ApiModelProperty(value = "环境信息")
    private Integer envType;

    @ApiModelProperty(value = "运行状态")
    private String runStatus;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;


}

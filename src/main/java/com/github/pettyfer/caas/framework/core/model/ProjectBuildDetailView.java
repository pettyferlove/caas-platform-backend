package com.github.pettyfer.caas.framework.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pettyfer.caas.global.constants.EnvConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "自动构建Model", description = "")
public class ProjectBuildDetailView implements Serializable {
    private static final long serialVersionUID = 5065510649631702102L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "项目Git Clone地址")
    @NotNull(message = "项目Clone地址不可为空")
    private String cloneUrl;

    @ApiModelProperty(value = "项目ID")
    @NotNull(message = "项目Clone地址不可为空")
    private String projectId;

    @ApiModelProperty(value = "项目名称")
    @NotNull(message = "项目名称不可为空")
    private String projectName;

    @ApiModelProperty(value = "源码仓库项目名称")
    private String sourceProjectName;

    @ApiModelProperty(value = "源码仓库Web访问地址")
    private String sourceProjectWebUrl;

    @ApiModelProperty(value = "镜像仓库别名")
    private String imagesDepositoryAlias;

    @ApiModelProperty(value = "源码拉取分支")
    private String branch;

    @ApiModelProperty(value = "触发条件")
    private String triggerMethod;

    @ApiModelProperty(value = "是否打开自动构建")
    private Integer openAutoBuild;

    @ApiModelProperty(value = "是否需要进行构建")
    @NotNull(message = "是否需要进行构建选项不可为空")
    private Integer needBuildProject;

    @ApiModelProperty(value = "构建工具")
    private String buildTool;

    @ApiModelProperty(value = "构建参数")
    private String buildParams;

    @ApiModelProperty(value = "Dockerfile是否存在")
    @NotNull(message = "Dockerfile是否存在选项不可为空")
    private Integer dockerfileAlreadyExists;

    @ApiModelProperty(value = "Dockerfile路径")
    private String dockerfilePath;

    @ApiModelProperty(value = "Dockerfile内容")
    private String dockerfileContent;

    @ApiModelProperty(value = "环境类型")
    private Integer envType = EnvConstant.DEVELOPMENT_ENVIRONMENT;

}

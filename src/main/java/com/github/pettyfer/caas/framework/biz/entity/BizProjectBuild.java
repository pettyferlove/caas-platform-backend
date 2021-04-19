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

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 项目构建配置
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
@ApiModel(value="BizProjectBuild对象", description="项目构建配置")
public class BizProjectBuild extends BaseEntity<BizProjectBuild> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "命名空间ID")
    @NotEmpty(message = "必须指定命名空间")
    private String namespaceId;

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "项目名称")
    @NotEmpty(message = "项目名称不能为空")
    private String projectName;

    @ApiModelProperty(value = "Clone地址")
    private String cloneUrl;

    @ApiModelProperty(value = "是否打开自动构建")
    private Integer openAutoBuild;

    @ApiModelProperty(value = "是否需要构建")
    private Integer needBuildProject;

    @ApiModelProperty(value = "构建工具")
    private String buildTool;

    @ApiModelProperty(value = "构建命令")
    private String buildCommand;

    @ApiModelProperty(value = "构建参数")
    private String buildParams;

    @ApiModelProperty(value = "是否构建镜像")
    private Integer needBuildImage;

    @ApiModelProperty(value = "构建目标路径（相对路径）")
    private String buildTargetPath;

    @ApiModelProperty(value = "构建目标文件夹或文件名")
    private String buildTargetName;

    @ApiModelProperty(value = "源码是否存在Dockerfile")
    private Integer dockerfileAlreadyExists;

    @ApiModelProperty(value = "源码Dockerfile相对路径")
    private String dockerfilePath;

    @ApiModelProperty(value = "Dockerfile内容（仅源码不存在Dockerfile时需要）")
    private String dockerfileContent;

    @ApiModelProperty(value = "Project触发器ID")
    private String projectHookId;

    @ApiModelProperty(value = "触发条件")
    private String triggerMethod;

    @ApiModelProperty(value = "源码拉取分支")
    private String branch;

    @ApiModelProperty(value = "镜像仓库别名")
    private String imagesDepositoryAlias;

    @ApiModelProperty(value = "源码仓库的项目名称")
    private String sourceProjectName;

    @ApiModelProperty(value = "源码仓库Web访问地址")
    private String sourceProjectWebUrl;

    @ApiModelProperty(value = "镜像仓库ID")
    private String imagesDepositoryId;

    @ApiModelProperty(value = "是否需要持久化构建文件")
    private Integer persistentBuildFile;

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

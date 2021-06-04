package com.github.pettyfer.caas.framework.biz.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 项目构建配置
 * </p>
 *
 * @author Petty
 * @since 2021-04-26
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

    @ApiModelProperty(value = "父项目ID")
    private String parentId;

    @TableField(exist = false)
    private String keywords;

    @ApiModelProperty(value = "是否需要关联其他项目")
    private Boolean linkProject;

    @ApiModelProperty(value = "命名空间ID")
    private String namespaceId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "描述信息")
    private String projectDescribe;

    @ApiModelProperty(value = "仓库类型")
    private String depositoryType;

    @ApiModelProperty(value = "远程仓库项目ID")
    private String remoteProjectId;

    @ApiModelProperty(value = "Clone地址")
    private String remotePath;

    @ApiModelProperty(value = "源码拉取分支")
    private String remoteBranch;

    @ApiModelProperty(value = "远程仓库地址空间")
    private String remoteOwner;

    @ApiModelProperty(value = "仓库路径（相对路径）")
    private String remoteRepo;

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

    @ApiModelProperty(value = "前置脚本")
    private String preShellScript;

    @ApiModelProperty(value = "后置脚本")
    private String postShellScript;

    @ApiModelProperty(value = "是否执行前置脚本")
    private Boolean runPreShellScript;

    @ApiModelProperty(value = "是否执行后置脚本")
    private Boolean runPostShellScript;

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

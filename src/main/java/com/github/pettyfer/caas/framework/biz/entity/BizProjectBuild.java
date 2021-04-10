package com.github.pettyfer.caas.framework.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <p>
 * 持续部署（CD）
 * </p>
 *
 * @author Petty
 * @since 2020-07-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="BizAutoBuild对象", description="自动构建")
public class BizProjectBuild extends Model<BizProjectBuild> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "命名空间")
    private String namespaceId;

    @ApiModelProperty(value = "项目ID")
    private String projectId;

    @ApiModelProperty(value = "项目触发器ID")
    private String projectHookId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "源码仓库项目名称")
    private String sourceProjectName;

    @ApiModelProperty(value = "源码仓库Web访问地址")
    private String sourceProjectWebUrl;

    @ApiModelProperty(value = "镜像仓库别名")
    private String imagesDepositoryAlias;

    private String imagesDepositoryId;

    private Integer persistentBuildFile;

    @ApiModelProperty(value = "源码拉取分支")
    private String branch;

    @ApiModelProperty(value = "触发条件")
    private String triggerMethod;

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

    @ApiModelProperty(value = "构建目标路径（相对路径）")
    private String buildTargetPath;

    @ApiModelProperty(value = "构建目标文件夹或文件名")
    private String buildTargetName;

    @ApiModelProperty(value = "是否构建镜像")
    private Integer needBuildImage;

    @ApiModelProperty(value = "源码是否存在Dockerfile")
    private Integer dockerfileAlreadyExists;

    @ApiModelProperty(value = "源码Dockerfile相对路径")
    private String dockerfilePath;

    @ApiModelProperty(value = "Dockerfile内容（仅源码不存在Dockerfile时需要）")
    private String dockerfileContent;

    @ApiModelProperty(value = "环境信息")
    private Integer envType;

    @ApiModelProperty(value = "删除标记 0 未删除 1 删除")
    @TableLogic
    private Boolean delFlag;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

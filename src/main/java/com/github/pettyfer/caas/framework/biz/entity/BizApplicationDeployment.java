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
 * 应用部署
 * </p>
 *
 * @author Petty
 * @since 2021-04-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "BizApplicationDeployment对象", description = "应用部署")
public class BizApplicationDeployment extends BaseEntity<BizApplicationDeployment> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "部署名称")
    private String name;

    @TableField(exist = false)
    private String keywords;

    @ApiModelProperty(value = "应用所属命名空间ID")
    private String namespaceId;

    @ApiModelProperty(value = "描述")
    private String descriptionContent;

    @ApiModelProperty(value = "自动构建项目ID，不使用自动构建则为空")
    private String autoBuildId;

    @ApiModelProperty(value = "镜像仓库ID")
    private String imagesDepositoryId;

    @ApiModelProperty(value = "镜像名称")
    private String imageName;

    @ApiModelProperty(value = "镜像Tag")
    private String imageTag;

    @ApiModelProperty(value = "应用环境变量")
    private String environmentVariable;

    @ApiModelProperty(value = "镜像拉取规则")
    private String imagePullStrategy;

    @ApiModelProperty(value = "实例数量")
    private Integer instancesNumber;

    @ApiModelProperty(value = "更新策略")
    private String strategyType;

    @ApiModelProperty(value = "升级过程中最多可以比原先设置多出的POD数量")
    private Integer maxSurge;

    @ApiModelProperty(value = "升级过程中最多有多少个POD处于无法提供服务的状态")
    private Integer maxUnavaible;

    @ApiModelProperty(value = "历史版本最多保存数量")
    private Integer revisionHistoryLimit;

    @ApiModelProperty(value = "指定部署节点")
    private String node;

    @ApiModelProperty(value = "是否开启存活探针")
    private Boolean openLivenessProbe;

    @ApiModelProperty(value = "是否开启就绪探针")
    private Boolean openReadinessProbe;

    @ApiModelProperty(value = "存活探针配置")
    private String livenessProbe;

    @ApiModelProperty(value = "就绪探针配置")
    private String readinessProbe;

    @ApiModelProperty(value = "环境类型")
    private Integer envType;

    @ApiModelProperty(value = "运行状态")
    private String runStatus;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

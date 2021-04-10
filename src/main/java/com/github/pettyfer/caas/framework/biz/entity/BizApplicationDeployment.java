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
 * 应用部署
 * </p>
 *
 * @author Petty
 * @since 2020-07-28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="BizApplicationDeployment对象", description="应用部署")
public class BizApplicationDeployment extends Model<BizApplicationDeployment> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "部署名称")
    private String name;

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

    @ApiModelProperty(value = "网络设置")
    private String network;

    @ApiModelProperty(value = "网络类型")
    private String networkType;

    @ApiModelProperty(value = "指定部署节点")
    private String node;

    @ApiModelProperty(value = "外部访问IP，以英文,分隔")
    private String externalIp;

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

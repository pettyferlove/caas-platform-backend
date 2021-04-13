package com.github.pettyfer.caas.framework.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "应用部署Model", description = "")
public class ApplicationDeploymentDetailView implements Serializable {
    private static final long serialVersionUID = -3932461522645875956L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "名称")
    @NotNull(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "详细描述")
    private String describe;

    @ApiModelProperty(value = "自动构建项目ID")
    private String autoBuildId;

    @ApiModelProperty(value = "镜像仓库ID")
    private String imagesDepositoryId;

    @ApiModelProperty(value = "镜像名称")
    @NotNull(message = "镜像名称不能为空")
    private String imageName;

    @ApiModelProperty(value = "镜像Tag")
    @NotNull(message = "镜像Tag不能为空")
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

    @ApiModelProperty(value = "网络设置")
    private String network;

    @ApiModelProperty(value = "网络类型")
    private String networkType;

    @ApiModelProperty(value = "指定部署节点")
    private String node;

    @ApiModelProperty(value = "外部访问IP，以英文,分隔")
    private String externalIp;

    @ApiModelProperty(value = "环境信息")
    private Integer envType;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    private Map<String, String> labels;

    @ApiModelProperty("可用副本")
    private Integer availableReplicas;

    @ApiModelProperty("就绪副本")
    private Integer readyReplicas;

    @ApiModelProperty("副本总数")
    private Integer replicas;

    @ApiModelProperty("不可用副本")
    private Integer unavailableReplicas;

    @ApiModelProperty("更新副本")
    private Integer updatedReplicas;

    private List<ApplicationDeploymentNetworkView> networks;

}

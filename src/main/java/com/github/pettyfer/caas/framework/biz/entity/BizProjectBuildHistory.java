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

import java.io.Serializable;

/**
 * <p>
 * 项目构建历史记录
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
@ApiModel(value="BizProjectBuildHistory对象", description="项目构建历史记录")
public class BizProjectBuildHistory extends BaseEntity<BizProjectBuildHistory> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    private String jobId;

    @ApiModelProperty(value = "构建ID")
    private String buildId;

    @ApiModelProperty(value = "镜像全名")
    private String imageFullName;

    @ApiModelProperty(value = "附件ID")
    private String fileId;

    @ApiModelProperty(value = "构建状态")
    private String buildStatus;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

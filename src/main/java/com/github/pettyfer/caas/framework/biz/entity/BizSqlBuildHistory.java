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
 * SQL构建历史记录
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
@ApiModel(value = "BizSqlBuildHistory对象", description = "SQL构建历史记录")
public class BizSqlBuildHistory extends BaseEntity<BizSqlBuildHistory> {

    private static final long serialVersionUID = 1L;

    private String jobId;

    private String buildId;

    private String fileId;

    private String buildStatus;

    private String start;

    private String end;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

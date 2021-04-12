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
 * SQL构建
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
@ApiModel(value="BizSqlBuild对象", description="SQL构建")
public class BizSqlBuild extends BaseEntity<BizSqlBuild> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "命名空间")
    private String namespaceId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目中文描述")
    private String describeInfo;

    @ApiModelProperty(value = "仓库类型")
    private Integer depositoryType;

    @ApiModelProperty(value = "远程仓库地址")
    private String remotePath;

    @ApiModelProperty(value = "远程分支")
    private String remoteBranch;

    @ApiModelProperty(value = "环境变量")
    private String env;

    @ApiModelProperty(value = "账号名")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "配置")
    private String config;

    @ApiModelProperty(value = "脚本生成起点")
    private String sqlFrom;

    @ApiModelProperty(value = "脚本生成终点")
    private String sqlTo;

    @ApiModelProperty(value = "环境类型")
    private Integer envType;

    @ApiModelProperty(value = "认证类型")
    private Integer authType;

    @ApiModelProperty(value = "全量构建")
    private Boolean full;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

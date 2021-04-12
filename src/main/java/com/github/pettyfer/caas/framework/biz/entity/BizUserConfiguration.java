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
 * 用户个人配置
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
@ApiModel(value="BizUserConfiguration对象", description="用户个人配置")
public class BizUserConfiguration extends BaseEntity<BizUserConfiguration> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "gitlab地址")
    private String gitlabHomePath;

    @ApiModelProperty(value = "Gitlab令牌")
    private String gitlabApiToken;

    @ApiModelProperty(value = "RSA私钥")
    private String privateKey;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @ApiModelProperty(value = "SSHkey ID")
    private Integer userKeyId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

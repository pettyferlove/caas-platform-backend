package com.github.pettyfer.caas.framework.biz.entity;

import com.github.pettyfer.caas.global.entity.BaseEntity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <p>
 * 
 * </p>
 *
 * @author Petty
 * @since 2021-03-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="BizUserConfiguration对象", description="")
public class BizUserConfiguration extends BaseEntity<BizUserConfiguration> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "gitlab地址")
    private String gitlabHomePath;

    @ApiModelProperty(value = "Gitlab令牌")
    private String gitlabApiToken;

    @ApiModelProperty(value = "RSA私钥")
    private String privateKey;

    @ApiModelProperty(value = "SSHkey ID")
    private Integer userKeyId;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

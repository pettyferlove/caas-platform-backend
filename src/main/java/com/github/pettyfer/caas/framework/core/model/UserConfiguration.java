package com.github.pettyfer.caas.framework.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="用户配置Model", description="")
public class UserConfiguration implements Serializable {
    private static final long serialVersionUID = -2858002591496343832L;

    @ApiModelProperty(value = "gitlab地址")
    private String gitlabHomePath;

    @ApiModelProperty(value = "Gitlab令牌")
    private String gitlabApiToken;

    @ApiModelProperty(value = "RSA私钥")
    private String privateKey;

    @ApiModelProperty(value = "SVN用户名")
    private String subversionUsername;

    @ApiModelProperty(value = "SVN密码")
    private String subversionPassword;

}

package com.github.pettyfer.caas.framework.system.entity;

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
import java.time.LocalDate;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author Petty
 * @since 2021-03-26
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "SystemUser对象", description = "用户信息")
public class SystemUser extends BaseEntity<SystemUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户登录名")
    private String loginName;

    @ApiModelProperty(value = "用户全名")
    private String userName;

    @ApiModelProperty(value = "账号密码")
    private String password;

    @ApiModelProperty(value = "性别")
    private Integer userSex;

    @ApiModelProperty(value = "生日")
    private LocalDate userBorn;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "电子邮件")
    private String email;

    @ApiModelProperty(value = "居住地址")
    private String userAddress;

    @ApiModelProperty(value = "移动电话")
    private String mobileTel;

    @ApiModelProperty(value = "用户联系电话")
    private String userTel;

    @ApiModelProperty(value = "用户证件类型")
    private String userIdenType;

    @ApiModelProperty(value = "证件ID")
    private String userIden;

    @ApiModelProperty(value = "是否有效 0 无效 1 有效")
    private Integer status;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

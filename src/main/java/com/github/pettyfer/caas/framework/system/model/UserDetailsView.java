package com.github.pettyfer.caas.framework.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "用户信息", description = "用户信息")
public class UserDetailsView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "用户登录名")
    @NotEmpty(message = "用户登录名不能为空")
    private String loginName;

    @ApiModelProperty(value = "用户全名")
    @NotEmpty(message = "用户全名不能为空")
    private String userName;

    @ApiModelProperty(value = "账号密码")
    @NotEmpty(message = "账号密码不能为空")
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

    @NotEmpty(message = "角色不能为空")
    private List<String> roleIds = new ArrayList<>();

}

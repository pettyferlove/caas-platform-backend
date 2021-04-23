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
import java.time.LocalDateTime;

/**
 * <p>
 * 系统消息
 * </p>
 *
 * @author Petty
 * @since 2021-04-23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "SystemMessage对象", description = "系统消息")
public class SystemMessage extends BaseEntity<SystemMessage> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息")
    private String message;

    @ApiModelProperty(value = "具体内容")
    private String content;

    @ApiModelProperty(value = "消息产生时间")
    private LocalDateTime time;

    @ApiModelProperty(value = "消息类型 success、error")
    private String type;

    @ApiModelProperty(value = "业务名称")
    private String businessName;

    @ApiModelProperty(value = "业务ID")
    private String businessId;

    @ApiModelProperty(value = "该业务对应的页面地址（对应前端路径）")
    private String businessPageAddress;

    @ApiModelProperty(value = "编辑地址（对应前端路径）")
    private String editPageAddress;

    @ApiModelProperty(value = "查看地址（对应前端路径）")
    private String viewPageAddress;

    @ApiModelProperty(value = "接收人USER_ID")
    private String receiver;

    @ApiModelProperty(value = "发送人USER_ID，如果是系统消息则为system")
    private String deliver;

    @ApiModelProperty(value = "状态 1已读 0未读 默认为0")
    private Integer state;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

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

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 持久化储存
 * </p>
 *
 * @author Petty
 * @since 2021-04-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="BizPersistentStorage对象", description="持久化储存")
public class BizPersistentStorage extends BaseEntity<BizPersistentStorage> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "应用所属命名空间ID")
    private String namespaceId;

    @ApiModelProperty(value = "持久化储存名称")
    @NotEmpty(message = "储存名称不可为空")
    private String name;

    @ApiModelProperty(value = "初始容量大小")
    @NotEmpty(message = "初始容量不可为空")
    private String initSize;

    @ApiModelProperty(value = "最大容量大小")
    private String limitSize;

    @ApiModelProperty(value = "容量单位")
    @NotEmpty(message = "容量单位不可为空")
    private String unit;

    @ApiModelProperty(value = "储存类名称")
    @NotEmpty(message = "储存类名称不可为空")
    private String storageClassName;

    @ApiModelProperty(value = "访问模式")
    @NotEmpty(message = "访问模式不可为空")
    private String accessMode;

    @ApiModelProperty(value = "环境类型")
    private Integer envType;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

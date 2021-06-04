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
 *
 * </p>
 *
 * @author Petty
 * @since 2021-06-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "BizKeyword对象", description = "")
public class BizKeyword extends BaseEntity<BizKeyword> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "关键词名称")
    @NotEmpty(message = "关键词名称必填")
    private String name;

    private String color;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

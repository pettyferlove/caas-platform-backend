package com.github.pettyfer.caas.framework.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "BizKeywordMap对象", description = "")
public class BizKeywordMap extends Model<BizKeywordMap> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "关键词ID")
    private String keywordId;

    @ApiModelProperty(value = "业务ID")
    private String bizId;

    @ApiModelProperty(value = "业务类型")
    private String bizType;

    @ApiModelProperty(value = "额外参数")
    private String additional;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

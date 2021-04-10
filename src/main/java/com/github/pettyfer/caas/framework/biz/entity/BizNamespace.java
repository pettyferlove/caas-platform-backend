package com.github.pettyfer.caas.framework.biz.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @since 2021-03-22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="BizNamespace对象", description="")
public class BizNamespace extends Model<BizNamespace> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @ApiModelProperty(value = "命名空间名称")
    private String name;

    @ApiModelProperty(value = "是否开启istio注入")
    private Boolean istio;

    @ApiModelProperty(value = "描述信息")
    private String description;

    @ApiModelProperty(value = "环境类型")
    private Integer envType;

    @ApiModelProperty(value = "仓库访问密钥")
    private String registrySecretName;

    @ApiModelProperty(value = "删除标记 0 未删除 1 删除")
    @TableLogic
    private Boolean delFlag;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    private LocalDateTime modifyTime;

    @ApiModelProperty(value = "项目组ID")
    private String groupId;

    @ApiModelProperty(value = "租户ID")
    private String tenantId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}

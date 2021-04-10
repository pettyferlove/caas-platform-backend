package com.github.pettyfer.caas.framework.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "数据构建列表Model", description = "")
public class SqlBuildListView implements Serializable {

    private static final long serialVersionUID = 9117121464853908191L;

    private String id;

    private String historyId;

    @ApiModelProperty(value = "命名空间")
    private String namespaceId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "项目中文描述")
    private String describeInfo;

    @ApiModelProperty(value = "仓库类型")
    private Integer depositoryType;

    @ApiModelProperty(value = "环境类型")
    private Integer envType;

    @ApiModelProperty(value = "最后一次构建状态")
    private String lastState;

    private String job;

    @ApiModelProperty(value = "最后一次构建时间")
    private LocalDateTime lastBuildTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}

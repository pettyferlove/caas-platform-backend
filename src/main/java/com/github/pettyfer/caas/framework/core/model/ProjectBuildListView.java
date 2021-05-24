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
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "持续集成查询列表Model", description = "")
public class ProjectBuildListView implements Serializable {
    private static final long serialVersionUID = 5065510649631702102L;

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "命名空间")
    private String namespaceId;

    private String historyId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "描述信息")
    private String projectDescribe;

    @ApiModelProperty(value = "环境类型")
    private Integer envType;

    @ApiModelProperty(value = "构建工具")
    private String buildTool;

    @ApiModelProperty(value = "源码仓库项目名称")
    private String sourceProjectName;

    @ApiModelProperty(value = "是否打开自动构建")
    private Integer openAutoBuild;

    @ApiModelProperty(value = "构建成功次数")
    private String successfulNumber;

    @ApiModelProperty(value = "构建失败次数")
    private String failuresNumber;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最后一次构建状态")
    private String lastState;

    private String job;

    @ApiModelProperty(value = "最后一次构建时间")
    private LocalDateTime lastBuildTime;

}

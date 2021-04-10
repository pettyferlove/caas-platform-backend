package com.github.pettyfer.caas.framework.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigListView {

    private String id;

    @ApiModelProperty(value = "配置名称")
    private String configName;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "配置文件名称")
    private String fileName;

    @ApiModelProperty(value = "配置类型")
    private String configType;

    @ApiModelProperty(value = "环境信息")
    private Integer envType;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}

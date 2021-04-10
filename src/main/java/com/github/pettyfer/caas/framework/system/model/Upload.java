package com.github.pettyfer.caas.framework.system.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.pettyfer.caas.global.constants.SecurityType;
import com.github.pettyfer.caas.global.constants.StorageType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "附件上传信息", description = "附件上传信息VO")
public class Upload implements Serializable {

    private static final long serialVersionUID = 3346764162292691065L;

    @NotEmpty(message = "请设置文件组")
    @ApiModelProperty("文件组（目录）")
    private String group;

    @ApiModelProperty("安全类型（针对云服务提供商）")
    private SecurityType security = SecurityType.Private;

    @NotNull(message = "请指定储存方式")
    @ApiModelProperty("储存方式")
    private StorageType storage;


}

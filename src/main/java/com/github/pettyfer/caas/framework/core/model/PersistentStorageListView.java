package com.github.pettyfer.caas.framework.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class PersistentStorageListView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "应用所属命名空间ID")
    private String namespaceId;

    @ApiModelProperty(value = "持久化储存名称")
    private String name;

    @ApiModelProperty(value = "初始容量大小")
    private String initSize;

    @ApiModelProperty(value = "最大容量大小")
    private String limitSize;

    @ApiModelProperty(value = "容量单位")
    private String unit;

    @ApiModelProperty(value = "储存类名称")
    private String storageClassName;

    @ApiModelProperty(value = "访问模式")
    private String accessMode;

    @ApiModelProperty(value = "环境类型")
    private Integer envType;

    private LocalDateTime createTime;

}

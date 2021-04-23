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

/**
 * <p>
 *
 * </p>
 *
 * @author Petty
 * @since 2021-04-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "SystemAttachmentInfo对象", description = "")
public class SystemAttachmentInfo extends BaseEntity<SystemAttachmentInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "文件类型")
    private String fileType;

    @ApiModelProperty(value = "文件大小")
    private Long size;

    @ApiModelProperty(value = "储存类型")
    private Integer storageType;

    @ApiModelProperty(value = "MD5值")
    private String md5;

    @ApiModelProperty(value = "地址")
    private String path;

    private String tenantId;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}

package com.github.pettyfer.caas.framework.engine.kubernetes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Pettyfer
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NamespaceCreate implements Serializable {
    private static final long serialVersionUID = 7931157356864358423L;

    /**
     * Namespace名称
     */
    @NotNull(message = "Namespace名称不能为空")
    @ApiModelProperty(value = "Namespace名称")
    private String name;

    /**
     * 是否开启Istio注入，默认关闭
     */
    @ApiModelProperty(value = "是否开启Istio注入")
    private Boolean istioInjection;

}

package com.github.pettyfer.caas.framework.engine.kubernetes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

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
@JsonPropertyOrder({"apiVersion", "kind", "metadata"})
public class NamespacePageView implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Namespace名称
     */
    private String name;

    /**
     * 运行状态
     */
    private String status;

    /**
     * 是否开启Istio注入，默认关闭
     */
    private Boolean istioInjection;

    /**
     * 创建时间
     */
    private String creationTimestamp;

}

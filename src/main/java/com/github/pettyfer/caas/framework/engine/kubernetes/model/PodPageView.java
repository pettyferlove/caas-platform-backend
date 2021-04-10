package com.github.pettyfer.caas.framework.engine.kubernetes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

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
public class PodPageView implements Serializable {

    private static final long serialVersionUID = -7014920611614507153L;

    private String name;

    private String nodeName;

    private String statusPhase;

    private String ip;

    private Integer restartCount;

    private String startTime;

}

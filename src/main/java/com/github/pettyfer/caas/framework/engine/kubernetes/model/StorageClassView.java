package com.github.pettyfer.caas.framework.engine.kubernetes.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class StorageClassView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String provisioner;

    private String reclaimPolicy;

    private LocalDateTime creationTimestamp;

}

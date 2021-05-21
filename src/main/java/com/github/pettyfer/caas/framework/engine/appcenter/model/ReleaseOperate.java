package com.github.pettyfer.caas.framework.engine.appcenter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author Pettyfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseOperate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty
    private String appRepositoryResourceName;

    @NotEmpty
    private String appRepositoryResourceNamespace;

    @NotEmpty
    private String chartName;

    @NotEmpty
    private String releaseName;

    @NotEmpty
    private String version;

    @NotEmpty
    private String values;

}

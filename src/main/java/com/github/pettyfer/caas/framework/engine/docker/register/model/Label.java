package com.github.pettyfer.caas.framework.engine.docker.register.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Label
 *
 * @author Petty
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Label implements Serializable {
    private static final long serialVersionUID = -1081533606662081186L;
    @JsonProperty("update_time")
    private String updateTime = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("color")
    private String color = null;

    @JsonProperty("creation_time")
    private String creationTime = null;

    @JsonProperty("deleted")
    private Boolean deleted = null;

    @JsonProperty("scope")
    private String scope = null;

    @JsonProperty("project_id")
    private Integer projectId = null;

    @JsonProperty("id")
    private Integer id = null;

    @JsonProperty("name")
    private String name = null;
}

